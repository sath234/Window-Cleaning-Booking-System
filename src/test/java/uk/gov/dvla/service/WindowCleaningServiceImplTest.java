package uk.gov.dvla.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.*;
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
    public void setUp() {
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

    // ========== addCustomer() Tests ==========
    
    @Test
    public void addCustomer_ValidCustomer_SavesSuccessfully() {
        Customer newCustomer = new Customer(5, "Yoko", 8);
        service.addCustomer(newCustomer);

        List<Customer> customers = customerDAO.findAll();
        assertEquals(5, customers.size());
    }

    @Test
    public void addCustomer_NullCustomer_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.addCustomer(null));
    }

    @Test
    public void addCustomer_DuplicateCustomer_ThrowsException() {
        Customer duplicate = new Customer(1, "Duplicate", 5);
        assertThrows(DuplicateEntityException.class, () ->
                service.addCustomer(duplicate));
    }

    // ========== getCustomer() Tests ==========
    
    @Test
    public void getCustomer_ValidCustomer_ReturnsCorrectCustomer() {
        Customer customer = service.getCustomer(1);

        assertEquals(new Customer(1, "John", 10), customer);
    }

    @Test
    public void getCustomer_NonExistentCustomer_ThrowsException() {
        assertThrows(CustomerNotFoundException.class, () ->
                service.getCustomer(999));
    }

    // ========== getCustomerIdByName() Tests ==========

    @Test
    public void getCustomerIdByName_ValidName_ReturnsCorrectCustomerId(){
        assertEquals(1, service.getCustomerIdByName("John"));
    }

    @Test
    public void getCustomerIdByName_NullName_ThrowsException(){
        assertThrows(NullPointerException.class, () -> service.getCustomerIdByName(null));
    }

    @Test
    public void getCustomerIdByName_NoCustomersReturned_ThrowsException(){
        assertThrows(CustomerNotFoundException.class, () -> service.getCustomerIdByName("Nathan"));
    }

    @Test
    public void getCustomerIdByName_MultipleCustomersRetuned_ThrowsException(){
        service.addCustomer(new Customer(5, "John", 10));
        assertThrows(MultipleCustomerFoundException.class, () -> service.getCustomerIdByName("John"));
    }

    // ========== getCustomerIdByName() Tests ==========
    
    @Test
    public void getCustomerIdByName_ExistingCustomer_ReturnsCorrectId() {
        int customerId = service.getCustomerIdByName("John");
        assertEquals(1, customerId);
    }

    @Test
    public void getCustomerIdByName_NullCustomerName_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getCustomerIdByName(null));
    }

    @Test
    public void getCustomerIdByName_CustomerDoesNotExist_ThrowsException() {
        assertThrows(CustomerNotFoundException.class, () ->
                service.getCustomerIdByName("Nathan"));
    }

    @Test
    public void getCustomerIdByName_MultipleCustomersReturned_ThrowsException() {
        service.addCustomer(new Customer(5, "John", 10));
        assertThrows(MultipleCustomerFoundException.class, () ->
                service.getCustomerIdByName("John"));
    }


    // ========== addBooking() Tests ==========
    
    @Test
    public void addBooking_ValidBooking_SavesSuccessfully() {
        Booking newBooking = new Booking(5, 1, LocalDate.of(2027, 1, 1));
        service.addBooking(newBooking);

        List<Booking> bookings = bookingDAO.findAll();
        assertEquals(5, bookings.size());
    }

    @Test
    public void addBooking_NullBooking_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.addBooking(null));
    }

    @Test
    public void addBooking_NonExistentCustomer_ThrowsException() {
        Booking booking = new Booking(5, 999, LocalDate.of(2027, 1, 1));
        assertThrows(CustomerNotFoundException.class, () ->
                service.addBooking(booking));
    }

    @Test
    public void addBooking_PastDate_ThrowsException() {
        Booking pastBooking = new Booking(5, 1, LocalDate.of(2020, 1, 1));
        assertThrows(BookingDateInThePastException.class, () ->
                service.addBooking(pastBooking));
    }

    // ========== getBookingByBookingId() Tests ==========
    
    @Test
    public void getBookingByBookingId_ExistingBooking_ReturnsBooking() {
        Booking booking = service.getBookingByBookingId(1);
        assertEquals(1, booking.getId());
        assertEquals(4, booking.getCustomerId());
    }

    @Test
    public void getBookingByBookingId_NonExistentBooking_ThrowsException() {
        assertThrows(BookingNotFoundException.class, () ->
                service.getBookingByBookingId(999));
    }

    // ========== getAllBookings() Tests ==========
    
    @Test
    public void getAllBooking_ReturnsAllBookings() {
        List<Booking> bookings = service.getAllBookings();
        assertEquals(4, bookings.size());
    }

    // ========== getAllBookingsForDate() Tests ==========
    
    @Test
    public void getAllBookingsForDate_ExistingDate_ReturnsBookings() {
        List<Booking> bookings = service.getAllBookingsForDate(LocalDate.of(2025, 10, 1));
        assertEquals(3, bookings.size());
    }

    @Test
    public void getAllBookingsForDate_NoBookings_ReturnsEmptyList() {
        List<Booking> bookings = service.getAllBookingsForDate(LocalDate.of(2030, 1, 1));
        assertTrue(bookings.isEmpty());
    }

    @Test
    public void getAllBookingsForDate_NullDate_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getAllBookingsForDate(null));
    }

    // ========== getAllBookingsForCustomerId() Tests ==========

    @Test
    public void getAllBookingsForCustomerId_CustomerWithOneBooking_ReturnsBookings() {
        List<Booking> bookings = service.getAllBookingsForCustomerId(1);
        assertEquals(1, bookings.size());
        assertEquals(3, bookings.get(0).getId());
    }

    @Test
    public void getAllBookingsForCustomerId_CustomerWithTwoBooking_ReturnsBookings() {
        service.addBooking(new Booking(5, 1, LocalDate.of(2025, 10, 1)));
        List<Booking> bookings = service.getAllBookingsForCustomerId(1);

        assertEquals(2, bookings.size());
        assertEquals(3, bookings.get(0).getId());
        assertEquals(5, bookings.get(1).getId());;
    }

    @Test
    public void getAllBookingsForCustomerId_CustomerWithNoBookings_ReturnsEmptyList() {
        assertThrows(CustomerNotFoundException.class, () ->{
            service.getAllBookingsForCustomerId(999);
        });
    }

    // ========== getAllBookingsForDateRange() Tests ==========

    @Test
    public void getAllBookingsForDateRange_MultipleBookings_ReturnsCorrectTotal() {
        List<Booking> bookings = service.getAllBookingsForDateRange(LocalDate.of(2025, 10, 1), LocalDate.of(2026, 10, 1));
        assertEquals(4, bookings.size());
        assertEquals(1, bookings.get(0).getId());
        assertEquals(2, bookings.get(1).getId());
        assertEquals(3, bookings.get(2).getId());
        assertEquals(4, bookings.get(3).getId());
    }

    @Test
    public void getAllBookingsForDateRange_SingleBookings_ReturnsCorrectTotal() {
        List<Booking> bookings = service.getAllBookingsForDateRange(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 10, 1));
        assertEquals(1, bookings.size());
        assertEquals(2, bookings.get(0).getId());
    }

    @Test
    public void getAllBookingsForDateRange_NoBookings_ReturnsCorrectTotal() {
        List<Booking> bookings = service.getAllBookingsForDateRange(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2));
        assertTrue(bookings.isEmpty());
    }

    @Test
    public void getAllBookingsForDateRange_NullStartDate_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getAllBookingsForDateRange(null, LocalDate.of(2020, 1, 1)));
    }

    @Test
    public void getAllBookingsForDateRange_NullEndDate_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getAllBookingsForDateRange(LocalDate.of(2020, 1, 1), null));
    }

    @Test
    public void getAllBookingsForDateRange_EndDateBeforeStartDate_ThrowsException() {
        assertThrows(InvalidDateRangeException.class, () ->
                service.getAllBookingsForDateRange(LocalDate.of(2020, 1, 1), LocalDate.of(2019, 1, 1)));
    }


    // ========== getTotalWindowsForDate() Tests ==========
    
    @Test
    public void getTotalWindowsForDate_MultipleBookings_ReturnsCorrectTotal() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        int result = service.getTotalWindowsForDate(date);
        assertEquals(26, result);
    }

    @Test
    public void getTotalWindowsForDate_SingleBooking_ReturnsCorrectTotal() {
        LocalDate date = LocalDate.of(2026, 1, 10);
        int result = service.getTotalWindowsForDate(date);
        assertEquals(5, result);
    }

    @Test
    public void getTotalWindowsForDate_NoBookings_ReturnsZero() {
        LocalDate date = LocalDate.of(2030, 1, 1);
        int result = service.getTotalWindowsForDate(date);
        assertEquals(0, result);
    }

    @Test
    public void getTotalWindowsForDate_NullDate_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getTotalWindowsForDate(null));
    }

    // ========== getBookingsWithCustomerName() Tests ==========

    @Test
    public void getBookingsWithCustomerName_WithValidName_ReturnsBooking() {
        List<Booking> bookings = service.getBookingsWithCustomerName("John");

        assertEquals(1, bookings.size());
        assertEquals(3, bookings.get(0).getId());
    }

    @Test
    public void getBookingsWithCustomerName_DoesNotHaveABooking_ReturnsBooking() {
        service.addCustomer(new Customer(5, "Nathan", 10));
        List<Booking> bookings = service.getBookingsWithCustomerName("Nathan");

        assertTrue(bookings.isEmpty());
    }

    @Test
    public void getBookingsWithCustomerName_NullName_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getBookingsWithCustomerName(null));
    }

    @Test
    public void getBookingsWithCustomerName_WithNonExistentName_ThrowsException() {
        assertThrows(CustomerNotFoundException.class, () ->
                service.getBookingsWithCustomerName("Nathan"));
    }

    @Test
    public void getBookingsWithCustomerName_MultipleCustomersReturned_ThrowsException() {
        service.addCustomer(new Customer(5, "John", 10));
        assertThrows(MultipleCustomerFoundException.class, () ->
                service.getBookingsWithCustomerName("John"));
    }

    // ========== getTotalWindowsForDateRange() Tests ==========

    @Test
    public void getTotalWindowsForDateRange_MultipleBookingsInDateRange_ReturnsCorrectTotal(){
        assertEquals(26, service.getTotalWindowsForDateRange(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 11, 1)));
        assertEquals(31, service.getTotalWindowsForDateRange(LocalDate.of(2025, 9, 1), LocalDate.of(2026, 11, 1)));
    }

    @Test
    public void getTotalWindowsForDateRange_SingleBookingInDateRange_ReturnsCorrectTotal(){
        assertEquals(5, service.getTotalWindowsForDateRange(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 11, 1)));
    }

    @Test
    public void getTotalWindowsForDateRange_NoBookingsInDateRange_ReturnsZero(){
        assertEquals(0, service.getTotalWindowsForDateRange(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 11, 1)));
    }

    @Test
    public void getTotalWindowsForDateRange_NullStartDate_ThrowsException(){
        assertThrows(NullPointerException.class, () ->{
            service.getTotalWindowsForDateRange(null, LocalDate.of(2020, 1, 1));
        });
    }

    @Test
    public void getTotalWindowsForDateRange_NullEndDate_ThrowsException(){
        assertThrows(NullPointerException.class, () ->{
            service.getTotalWindowsForDateRange(LocalDate.of(2020, 1, 1), null);
        });
    }

    @Test
    public void getTotalWindowsForDateRange_StartDateAfterEndDate_ThrowsException(){
        assertThrows(InvalidDateRangeException.class, () ->{
            service.getTotalWindowsForDateRange(LocalDate.of(2020, 1, 3), LocalDate.of(2020, 1, 2));
        });
    }

    @Test
    public void getTotalWindowsForDateRange_MissingCustomer_ThrowsException(){
        bookingDAO.save(new Booking(98, 999, LocalDate.of(2026, 1, 1)));
        assertThrows(CustomerNotFoundException.class, () ->{
            assertEquals(5, service.getTotalWindowsForDateRange(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 11, 1)));
        });
    }

    // ========== getTotalCostForBooking() Tests ==========
    
    @Test
    public void getTotalCostForBooking_GeorgeBooking_ReturnsCorrectCost() {
        int result = service.getTotalCostForBooking(1);
        assertEquals(9, result);
    }

    @Test
    public void getTotalCostForBooking_JohnBooking_ReturnsCorrectCost() {
        int result = service.getTotalCostForBooking(3);
        assertEquals(15, result);
    }

    @Test
    public void getTotalCostForBooking_PaulBooking_ReturnsCorrectCost() {
        int result = service.getTotalCostForBooking(2);
        assertEquals(10, result);
    }

    @Test
    public void getTotalCostForBooking_BookingNotFound_ThrowsException() {
        assertThrows(BookingNotFoundException.class, () ->
                service.getTotalCostForBooking(999));
    }

    @Test
    public void getTotalCostForBooking_CustomerNotFound_ThrowsCustomerNotFoundException() {
        bookingDAO.save(new Booking(98, 999, LocalDate.of(2027, 1, 1)));

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () ->
                service.getTotalCostForBooking(98)
        );

        assertEquals("No customer found", exception.getMessage());
    }

    // ========== getTotalCostForDate() Tests ==========
    
    @Test
    public void getTotalCostForDate_MultipleBookings_ReturnsCorrectTotal() {
        int result = service.getTotalCostForDate(LocalDate.of(2025, 10, 1));
        assertEquals(41, result);
    }

    @Test
    public void getTotalCostForDate_SingleBooking_ReturnsCorrectTotal() {
        int result = service.getTotalCostForDate(LocalDate.of(2026, 1, 10));
        assertEquals(10, result);
    }

    @Test
    public void getTotalCostForDate_NoBookings_ReturnsZero() {
        int result = service.getTotalCostForDate(LocalDate.of(2030, 1, 1));
        assertEquals(0, result);
    }

    // ========== getTotalCostForDateRange() Tests ==========

    @Test
    public void getTotalCostForDateRange_MultipleBookingsInDateRange_ReturnsCorrectTotal() {
        int result = service.getTotalCostForDateRange(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 11, 1));
        assertEquals(41, result);
    }

    @Test
    public void getTotalCostForDateRange_SingleBookingsInDateRange_ReturnsCorrectTotal() {
        int result = service.getTotalCostForDateRange(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 11, 1));
        assertEquals(10, result);
    }

    @Test
    public void getTotalCostForDateRange_NoBookingsInDateRange_ReturnsCorrectTotal() {
        int result = service.getTotalCostForDateRange(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 12, 1));
        assertEquals(0, result);
    }

    @Test
    public void getTotalCostForDateRange_NullStartDate_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getTotalCostForDateRange(null, LocalDate.of(2020, 1, 1)));
    }

    @Test
    public void getTotalCostForDateRange_NullEndDate_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                service.getTotalCostForDateRange(LocalDate.of(2020, 1, 1), null));
    }

    @Test
    public void getTotalCostForDateRange_StartDateAfterEndDate_ThrowsException() {
        assertThrows(InvalidDateRangeException.class, () ->
                service.getTotalCostForDateRange(LocalDate.of(2020, 1, 1), LocalDate.of(2019, 1, 1)));
    }

    @Test
    public void getTotalCostForDateRange_CustomerNotFound_ThrowsException() {
        bookingDAO.save(new Booking(98, 999, LocalDate.of(2026, 1, 1)));
        assertThrows(CustomerNotFoundException.class, () ->
                service.getTotalCostForDateRange(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 1)));
    }

    // ========== Edge Case Tests ==========
    @Test
    public void addCustomer_CustomerWithZeroWindows_ThrowsException() {
        assertThrows(InvalidCustomerException.class, () ->{
            service.addCustomer(new Customer(5, "Zero Windows", 0));
        });
    }

    @Test
    public void addCustomer_NullName_ThrowsException() {
        assertThrows(InvalidCustomerException.class, () ->{
            service.addCustomer(new Customer(5, null, 5));
        });
    }

    @Test
    public void addCustomer_IdLessThanOne_ThrowsException() {
        assertThrows(InvalidCustomerException.class, () ->{
            service.addCustomer(new Customer(0, null, 5));
        });

        assertThrows(InvalidCustomerException.class, () ->{
            service.addCustomer(new Customer(-1, null, 5));
        });
    }

    @Test
    public void addBooking_BookingIdLessThan1_ThrowsException() {
        assertThrows(InvalidBookingException.class, () ->{
            service.addBooking(new Booking(0, 1, LocalDate.of(2026, 1, 1)));
        });
        assertThrows(InvalidBookingException.class, () ->{
            service.addBooking(new Booking(-1, 1, LocalDate.of(2026, 1, 1)));
        });
    }

    @Test
    public void addBooking_CustomerIdLessThan1_ThrowsException() {
        assertThrows(InvalidBookingException.class, () ->{
            service.addBooking(new Booking(1, 0, LocalDate.of(2026, 1, 1)));
        });
        assertThrows(InvalidBookingException.class, () ->{
            service.addBooking(new Booking(1, -1, LocalDate.of(2026, 1, 1)));
        });
    }

    @Test
    public void addBooking_BookingDateNull_ThrowsException() {
        assertThrows(InvalidBookingException.class, () ->{
            service.addBooking(new Booking(6, 1, null));
        });
    }
}