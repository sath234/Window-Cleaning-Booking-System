package uk.gov.dvla.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.BookingNotFoundException;
import uk.gov.dvla.model.exception.CustomerNotFoundException;
import uk.gov.dvla.persistence.BookingDAO;
import uk.gov.dvla.persistence.BookingDAOImpl;
import uk.gov.dvla.persistence.CustomerDAO;
import uk.gov.dvla.persistence.CustomerDAOImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceImplTest {

    private BookingDAO bookingDAO;
    private CustomerDAO customerDAO;
    private BookingServiceImpl bookingService;
    
    @BeforeEach
    void setUp() {
        bookingDAO = new BookingDAOImpl();
        customerDAO = new CustomerDAOImpl();
        bookingService = new BookingServiceImpl(bookingDAO, customerDAO);
        setupTestData();
    }
    
    private void setupTestData() {
        customerDAO.save(new Customer(1, "John", 10));
        customerDAO.save(new Customer(2, "Paul", 5));
        customerDAO.save(new Customer(3, "Ringo", 12));
        customerDAO.save(new Customer(4, "George", 4));
        
        bookingDAO.save(new Booking(1, 4, LocalDate.of(2025, 10, 1)));
        bookingDAO.save(new Booking(2, 2, LocalDate.of(2026, 1, 10)));
        bookingDAO.save(new Booking(3, 1, LocalDate.of(2025, 10, 1)));
        bookingDAO.save(new Booking(4, 3, LocalDate.of(2025, 10, 1)));
    }

    @Test
    void getTotalWindowsForDate_MultipleBookings_ReturnsCorrectTotal() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        int result = bookingService.getTotalWindowsForDate(date);
        assertEquals(26, result);
    }
    
    @Test
    void getTotalWindowsForDate_SingleBooking_ReturnsCorrectTotal() {
        LocalDate date = LocalDate.of(2026, 1, 10);
        int result = bookingService.getTotalWindowsForDate(date);
        assertEquals(5, result);
    }
    
    @Test
    void getTotalWindowsForDate_NoBookings_ReturnsZero() {
        LocalDate date = LocalDate.of(2030, 1, 1);
        int result = bookingService.getTotalWindowsForDate(date);
        assertEquals(0, result);
    }
    
    @Test
    void getTotalWindowsForDate_NullDate_ThrowsException() {
        assertThrows(RuntimeException.class, () -> 
            bookingService.getTotalWindowsForDate(null));
    }
    
    @Test
    void getTotalWindowsForDate_CustomerNotFound_ThrowsCustomerNotFoundException() {
        LocalDate date = LocalDate.of(2027, 1, 1);
        bookingDAO.save(new Booking(99, 999, date));
        
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () ->
            bookingService.getTotalWindowsForDate(date));
        
        assertEquals("No customer found", exception.getMessage());
    }
    
    @Test
    void getTotalCostForBooking_GeorgeBooking_ReturnsCorrectCost() {
        int result = bookingService.getTotalCostForBooking(1);
        assertEquals(9, result);
    }
    
    @Test
    void getTotalCostForBooking_JohnBooking_ReturnsCorrectCost() {
        int result = bookingService.getTotalCostForBooking(3);
        assertEquals(15, result);
    }
    
    @Test
    void getTotalCostForBooking_PaulBooking_ReturnsCorrectCost() {
        int result = bookingService.getTotalCostForBooking(2);
        assertEquals(10, result);
    }
    
    @Test
    void getTotalCostForBooking_BookingNotFound_ThrowsBookingNotFoundException() {
        BookingNotFoundException exception = assertThrows(BookingNotFoundException.class, () ->
            bookingService.getTotalCostForBooking(999));
        
        assertEquals("No booking found", exception.getMessage());
    }
    
    @Test
    void getTotalCostForBooking_CustomerNotFound_ThrowsCustomerNotFoundException() {
        bookingDAO.save(new Booking(98, 999, LocalDate.of(2027, 1, 1)));
        
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () ->
            bookingService.getTotalCostForBooking(98));
        
        assertEquals("No customer found", exception.getMessage());
    }
    
    @Test
    void getTotalCostForBooking_CustomerWithZeroWindows_ReturnsPropertyCostOnly() {
        customerDAO.save(new Customer(5, "Zero Windows", 0));
        bookingDAO.save(new Booking(97, 5, LocalDate.of(2027, 1, 1)));
        
        int result = bookingService.getTotalCostForBooking(97);
        assertEquals(5, result); 
    }
}