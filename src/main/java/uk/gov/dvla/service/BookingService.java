package uk.gov.dvla.service;

import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.exception.BookingNotFoundException;
import uk.gov.dvla.model.exception.CustomerNotFoundException;
import uk.gov.dvla.model.exception.DuplicateEntityException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing booking operations and calculations.
 */
public interface BookingService {
    
    /**
     * Adds a new booking to the system.
     *
     * @param booking the booking to add
     * @throws RuntimeException if booking is null or date is in the past
     * @throws CustomerNotFoundException if customer doesn't exist
     * @throws DuplicateEntityException if booking already exists
     */
    void addBooking(Booking booking);
    
    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingNumber the booking ID
     * @return the booking
     * @throws BookingNotFoundException if booking not found
     */
    Booking getBookingByBookingId(int bookingNumber);
    
    /**
     * Retrieves all bookings in the system.
     *
     * @return list of all bookings
     */
    List<Booking> getAllBooking();
    
    /**
     * Retrieves all bookings for a specific date.
     *
     * @param date the date to search for
     * @return list of bookings for the date
     * @throws RuntimeException if date is null
     */
    List<Booking> getAllBookingForDate(LocalDate date);
    
    /**
     * Retrieves all bookings for a specific customer.
     *
     * @param customerId the customer ID
     * @return list of bookings for the customer
     */
    List<Booking> getAllBookingForCustomerId(int customerId);
    
    /**
     * Retrieves all bookings within a date range.
     *
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of bookings in the date range
     * @throws RuntimeException if startDate or endDate is null
     */
    List<Booking> getAllBookingForDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Calculates the total number of windows to be cleaned on a specific date.
     *
     * @param date the date to calculate for
     * @return total number of windows
     * @throws NullPointerException if date is null
     * @throws CustomerNotFoundException if any customer not found
     */
    int getTotalWindowsForDate(LocalDate date);
    
    /**
     * Calculates the total number of windows to be cleaned within a date range.
     *
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return total number of windows
     * @throws NullPointerException if startDate or endDate is null
     * @throws CustomerNotFoundException if any customer not found
     */
    int getTotalWindowsForDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Calculates the total cost for a specific booking.
     * Cost = (number of windows × £1) + £5 per property.
     *
     * @param bookingNumber the booking ID
     * @return total cost in pounds
     * @throws BookingNotFoundException if booking not found
     * @throws CustomerNotFoundException if customer not found
     */
    int getTotalCostForBooking(int bookingNumber);
    
    /**
     * Calculates the total cost for all bookings on a specific date.
     *
     * @param date the date to calculate for
     * @return total cost in pounds
     * @throws NullPointerException if date is null
     * @throws CustomerNotFoundException if any customer not found
     */
    int getTotalCostForDate(LocalDate date);
    
    /**
     * Calculates the total cost for all bookings within a date range.
     *
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return total cost in pounds
     * @throws RuntimeException if startDate or endDate is null
     * @throws CustomerNotFoundException if any customer not found
     */
    int getTotalCostForDateRange(LocalDate startDate, LocalDate endDate);
}