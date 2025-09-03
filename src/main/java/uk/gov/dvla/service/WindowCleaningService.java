package uk.gov.dvla.service;

import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.BookingNotFoundException;
import uk.gov.dvla.model.exception.CustomerNotFoundException;
import uk.gov.dvla.model.exception.DuplicateEntityException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for window cleaning operations.
 * Provides a unified interface for all window cleaning business operations
 * including customer management, booking management, and cost calculations.
 */
public interface WindowCleaningService {
    /**
     * Adds a new customer to the system.
     *
     * @param customer the customer to add
     * @throws NullPointerException     if customer is null
     * @throws DuplicateEntityException if customer already exists
     */
    void addCustomer(Customer customer);

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId the customer ID
     * @return the customer
     * @throws CustomerNotFoundException if customer not found
     */
    Customer getCustomer(int customerId);

    /**
     * Finds a customer ID by their name.
     *
     * @param name the customer name
     * @return the customer ID
     * @throws NullPointerException      if name is null
     * @throws CustomerNotFoundException if customer not found
     */
    int getCustomerIdByName(String name);

    /**
     * Adds a new booking to the system.
     *
     * @param booking the booking to add
     * @throws NullPointerException      if booking is null
     * @throws CustomerNotFoundException if customer doesn't exist
     * @throws DuplicateEntityException  if booking already exists
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
    List<Booking> getAllBookings();

    /**
     * Retrieves all bookings for a specific date.
     *
     * @param date the date to search for
     * @return list of bookings for the date
     * @throws NullPointerException if date is null
     */
    List<Booking> getAllBookingsForDate(LocalDate date);

    /**
     * Retrieves all bookings for a specific customer.
     *
     * @param customerId the customer ID
     * @return list of bookings for the customer
     */
    List<Booking> getAllBookingsForCustomerId(int customerId);

    /**
     * Retrieves all bookings within a date range.
     *
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return list of bookings in the date range
     * @throws RuntimeException if startDate or endDate is null
     */
    List<Booking> getAllBookingsForDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the total number of windows to be cleaned on a specific date.
     *
     * @param date the date to calculate for
     * @return total number of windows
     * @throws NullPointerException      if date is null
     * @throws CustomerNotFoundException if any customer not found
     */
    int getTotalWindowsForDate(LocalDate date);

    /**
     * Calculates the total number of windows to be cleaned within a date range.
     *
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return total number of windows
     * @throws NullPointerException      if startDate or endDate is null
     * @throws CustomerNotFoundException if any customer not found
     */
    int getTotalWindowsForDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the total cost for a specific booking.
     * Cost = (number of windows × £1) + £5 per property.
     *
     * @param bookingNumber the booking ID
     * @return total cost in pounds
     * @throws BookingNotFoundException  if booking not found
     * @throws CustomerNotFoundException if customer not found
     */
    int getTotalCostForBooking(int bookingNumber);

    /**
     * Calculates the total cost for all bookings on a specific date.
     *
     * @param date the date to calculate for
     * @return total cost in pounds
     * @throws NullPointerException      if date is null
     * @throws CustomerNotFoundException if any customer not found
     */
    int getTotalCostForDate(LocalDate date);

    /**
     * Calculates the total cost for all bookings within a date range.
     *
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return total cost in pounds
     * @throws RuntimeException          if startDate or endDate is null
     * @throws CustomerNotFoundException if any customer not found
     */
    int getTotalCostForDateRange(LocalDate startDate, LocalDate endDate);
}
