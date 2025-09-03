package uk.gov.dvla.persistence;

import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.exception.DuplicateEntityException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Booking entities.
 * Provides CRUD operations and query methods for bookings.
 */
public interface BookingDAO {
    
    /**
     * Saves a booking to the data store.
     *
     * @param booking the booking to save
     * @throws DuplicateEntityException if booking already exists
     */
    void save(Booking booking);

    /**
     * Finds a booking by its ID.
     *
     * @param bookingId the booking ID
     * @return Optional containing the booking if found, empty otherwise
     */
    Optional<Booking> findById(int bookingId);

    /**
     * Retrieves all bookings.
     *
     * @return list of all bookings
     */
    List<Booking> findAll();

    /**
     * Finds all bookings for a specific date.
     *
     * @param date the date to search for
     * @return list of bookings for the date
     */
    List<Booking> findByDate(LocalDate date);

    /**
     * Finds all bookings for a specific customer ID.
     *
     * @param customerId the customer ID to search for
     * @return list of bookings for the customer
     */
    List<Booking> findByCustomerId(int customerId);

    /**
     * Finds all bookings within a date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return list of bookings within the date range
     */
    List<Booking> findByDateRange(LocalDate startDate, LocalDate endDate);
}
