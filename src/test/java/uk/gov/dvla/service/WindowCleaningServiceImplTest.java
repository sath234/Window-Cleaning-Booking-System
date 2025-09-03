package uk.gov.dvla.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.BookingNotFoundException;
import uk.gov.dvla.model.exception.CustomerNotFoundException;
import uk.gov.dvla.model.exception.DuplicateEntityException;
import uk.gov.dvla.model.exception.MultipleCustomerFoundException;
import uk.gov.dvla.persistence.BookingDAO;
import uk.gov.dvla.persistence.BookingDAOImpl;
import uk.gov.dvla.persistence.CustomerDAO;
import uk.gov.dvla.persistence.CustomerDAOImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WindowCleaningServiceImplTest {

    private BookingDAO bookingDAO;
    private CustomerDAO customerDAO;
    private WindowCleaningServiceImpl service;

    @BeforeEach
    void setUp() {
        bookingDAO = new BookingDAOImpl();
        customerDAO = new CustomerDAOImpl();
        service = new WindowCleaningServiceImpl(bookingDAO, customerDAO);
        setupTestData();
    }

    private void setupTestData() {
        service.addCustomer(new Customer(1, "John", 10));
        service.addCustomer(new Customer(2, "Paul", 5));
        service.addCustomer(new Customer(3, "Ringo", 12));
        service.addCustomer(new Customer(4, "George", 4));

        service.addBooking(new Booking(1, 4, LocalDate.of(2025, 10, 1)));
        service.addBooking(new Booking(2, 2, LocalDate.of(2026, 1, 10)));
        service.addBooking(new Booking(3, 1, LocalDate.of(2025, 10, 1)));
        service.addBooking(new Booking(4, 3, LocalDate.of(2025, 10, 1)));
    }

    @Test
    void addCustomer_ValidCustomer_SavesSuccessfully() {
        Customer newCustomer = new Customer(5, "Yoko", 8);
        service.addCustomer(newCustomer);

        List<Customer> customers = customerDAO.findAll();
        assertEquals(5, customers.size());
    }

    @Test
    void addCustomer_NullCustomer_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                service.addCustomer(null));
    }

    @Test
    void addCustomer_DuplicateCustomer_ThrowsException() {
        Customer duplicate = new Customer(1, "Duplicate", 5);
        assertThrows(DuplicateEntityException.class, () ->
                service.addCustomer(duplicate));
    }

    @Test
    void getCustomer_ValidCustomer_ReturnsCorrectCustomer() {
        Customer customer = service.getCustomer(1);

        assertEquals(new Customer(1, "John", 10), customer);
    }

    @Test
    void getCustomer_NonExistentCustomer_ThrowsException() {
        assertThrows(CustomerNotFoundException.class, () ->
                service.getCustomer(999));
    }

    @Test
    void getCustomerIdByName_ExistingCustomer_ReturnsCorrectId() {
        int customerId = service.getCustomerIdByName("John");
        assertEquals(1, customerId);
    }

    @Test
    void getCustomerIdByName_NullCustomerName_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getCustomerIdByName(null));
    }

    @Test
    void getCustomerIdByName_CustomerDoesNotExist_ThrowsException() {
        assertThrows(CustomerNotFoundException.class, () ->
                service.getCustomerIdByName("Nathan"));
    }

    @Test
    void getCustomerIdByName_MultipleCustomersReturned_ThrowsException() {
        service.addCustomer(new Customer(5, "John", 10));
        assertThrows(MultipleCustomerFoundException.class, () ->
                service.getCustomerIdByName("John"));
    }


    @Test
    void addBooking_ValidBooking_SavesSuccessfully() {
        Booking newBooking = new Booking(5, 1, LocalDate.of(2027, 1, 1));
        service.addBooking(newBooking);

        List<Booking> bookings = bookingDAO.findAll();
        assertEquals(5, bookings.size());
    }

    @Test
    void addBooking_NullBooking_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                service.addBooking(null));
    }

    @Test
    void addBooking_NonExistentCustomer_ThrowsException() {
        Booking booking = new Booking(5, 999, LocalDate.of(2027, 1, 1));
        assertThrows(CustomerNotFoundException.class, () ->
                service.addBooking(booking));
    }

    @Test
    void addBooking_PastDate_ThrowsException() {
        Booking pastBooking = new Booking(5, 1, LocalDate.of(2020, 1, 1));
        assertThrows(RuntimeException.class, () ->
                service.addBooking(pastBooking));
    }

    @Test
    void getBookingByBookingId_ExistingBooking_ReturnsBooking() {
        Booking booking = service.getBookingByBookingId(1);
        assertEquals(1, booking.getId());
        assertEquals(4, booking.getCustomerId());
    }

    @Test
    void getBookingByBookingId_NonExistentBooking_ThrowsException() {
        assertThrows(BookingNotFoundException.class, () ->
                service.getBookingByBookingId(999));
    }

    @Test
    void getAllBooking_ReturnsAllBookings() {
        List<Booking> bookings = service.getAllBooking();
        assertEquals(4, bookings.size());
    }

    @Test
    void getAllBookingForDate_ExistingDate_ReturnsBookings() {
        List<Booking> bookings = service.getAllBookingForDate(LocalDate.of(2025, 10, 1));
        assertEquals(3, bookings.size());
    }

    @Test
    void getAllBookingForDate_NoBookings_ReturnsEmptyList() {
        List<Booking> bookings = service.getAllBookingForDate(LocalDate.of(2030, 1, 1));
        assertTrue(bookings.isEmpty());
    }

    @Test
    void getAllBookingForDate_NullDate_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                service.getAllBookingForDate(null));
    }

    @Test
    void getAllBookingForCustomerId_OneCustomer_ReturnsBookings() {
        List<Booking> bookings = service.getAllBookingForCustomerId(1);
        assertEquals(1, bookings.size());
        assertEquals(3, bookings.get(0).getId());
    }

    @Test
    void getAllBookingForCustomerId_twpCustomer_ReturnsBookings() {
        service.addBooking(new Booking(5, 1, LocalDate.of(2025, 10, 1)));

        List<Booking> bookings = service.getAllBookingForCustomerId(1);
        assertEquals(2, bookings.size());
        assertEquals(3, bookings.get(0).getId());
        assertEquals(5, bookings.get(1).getId());
    }

    @Test
    void getAllBookingForCustomerId_NoCustomers_ReturnsBookings() {
        List<Booking> bookings = service.getAllBookingForCustomerId(999);
        assertTrue(bookings.isEmpty());
    }

    @Test
    void getAllBookingForDateRange_ValidRange_ReturnsBookings() {
        List<Booking> bookings = service.getAllBookingForDateRange(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        );
        assertEquals(3, bookings.size());
    }

    @Test
    void getAllBookingForDateRange_NoBookingsInRange_ReturnsEmptyList() {
        List<Booking> bookings = service.getAllBookingForDateRange(
                LocalDate.of(2030, 1, 1),
                LocalDate.of(2030, 12, 31)
        );
        assertTrue(bookings.isEmpty());
    }

    @Test
    void getAllBookingForDateRange_NullStartDate_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                service.getAllBookingForDateRange(null, LocalDate.of(2025, 12, 31)));
    }

    @Test
    void getAllBookingForDateRange_NullEndDate_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                service.getAllBookingForDateRange(LocalDate.of(2025, 1, 1), null));
    }

    @Test
    void getTotalWindowsForDate_MultipleBookings_ReturnsCorrectTotal() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        int result = service.getTotalWindowsForDate(date);
        assertEquals(26, result);
    }

    @Test
    void getTotalWindowsForDate_SingleBooking_ReturnsCorrectTotal() {
        LocalDate date = LocalDate.of(2026, 1, 10);
        int result = service.getTotalWindowsForDate(date);
        assertEquals(5, result);
    }

    @Test
    void getTotalWindowsForDate_NoBookings_ReturnsZero() {
        LocalDate date = LocalDate.of(2030, 1, 1);
        int result = service.getTotalWindowsForDate(date);
        assertEquals(0, result);
    }

    @Test
    void getTotalWindowsForDate_NullDate_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                service.getTotalWindowsForDate(null));
    }

    @Test
    void getTotalWindowsForDateRange_ValidRange_ReturnsCorrectTotal() {
        int result = service.getTotalWindowsForDateRange(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        );
        assertEquals(26, result);
    }

    @Test
    void getTotalWindowsForDateRange_NoBookingsInRange_ReturnsZero() {
        int result = service.getTotalWindowsForDateRange(
                LocalDate.of(2030, 1, 1),
                LocalDate.of(2030, 12, 31)
        );
        assertEquals(0, result);
    }

    @Test
    void getTotalWindowsForDateRange_NullStartDate_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                service.getTotalWindowsForDateRange(null, LocalDate.of(2025, 12, 31)));
    }

    @Test
    void getTotalWindowsForDateRange_NullEndDate_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                service.getTotalWindowsForDateRange(LocalDate.of(2025, 1, 1), null));
    }

    @Test
    void getTotalCostForBooking_GeorgeBooking_ReturnsCorrectCost() {
        int result = service.getTotalCostForBooking(1);
        assertEquals(9, result);
    }

    @Test
    void getTotalCostForBooking_JohnBooking_ReturnsCorrectCost() {
        int result = service.getTotalCostForBooking(3);
        assertEquals(15, result);
    }

    @Test
    void getTotalCostForBooking_PaulBooking_ReturnsCorrectCost() {
        int result = service.getTotalCostForBooking(2);
        assertEquals(10, result);
    }

    @Test
    void getTotalCostForBooking_BookingNotFound_ThrowsException() {
        assertThrows(BookingNotFoundException.class, () ->
                service.getTotalCostForBooking(999));
    }

    @Test
    void getTotalCostForBooking_CustomerNotFound_ThrowsCustomerNotFoundException() {
        bookingDAO.save(new Booking(98, 999, LocalDate.of(2027, 1, 1)));

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () ->
                service.getTotalCostForBooking(98)
        );

        assertEquals("No customer found", exception.getMessage());
    }

    @Test
    void getTotalCostForDate_MultipleBookings_ReturnsCorrectTotal() {
        int result = service.getTotalCostForDate(LocalDate.of(2025, 10, 1));
        assertEquals(41, result);
    }

    @Test
    void getTotalCostForDate_SingleBooking_ReturnsCorrectTotal() {
        int result = service.getTotalCostForDate(LocalDate.of(2026, 1, 10));
        assertEquals(10, result);
    }

    @Test
    void getTotalCostForDate_NoBookings_ReturnsZero() {
        int result = service.getTotalCostForDate(LocalDate.of(2030, 1, 1));
        assertEquals(0, result);
    }

    @Test
    void getTotalCostForDateRange_ValidRange_ReturnsCorrectTotal() {
        int result = service.getTotalCostForDateRange(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 12, 31)
        );
        assertEquals(51, result);
    }

    @Test
    void getTotalCostForDateRange_NoBookingsInRange_ReturnsZero() {
        int result = service.getTotalCostForDateRange(
                LocalDate.of(2030, 1, 1),
                LocalDate.of(2030, 12, 31)
        );
        assertEquals(0, result);
    }

    @Test
    void addBooking_CustomerWithZeroWindows_WorksCorrectly() {
        service.addCustomer(new Customer(5, "Zero Windows", 0));
        service.addBooking(new Booking(97, 5, LocalDate.of(2027, 1, 1)));

        int cost = service.getTotalCostForBooking(97);
        assertEquals(5, cost);
    }
}