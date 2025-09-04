package uk.gov.dvla.persistence;

import lombok.NoArgsConstructor;
import uk.gov.dvla.model.Booking;
import uk.gov.dvla.util.ValidationUtil;

import java.time.LocalDate;
import java.util.*;

/**
 * Implementation of BookingDAO interface.
 * This class provides methods to manage bookings in memory database.
 */
public class BookingDAOImpl implements BookingDAO {

    /**
     * Map of Bookings representing bookings in memory database.
     * The key is the booking id and the value is the Booking object.
     */
    private final Map<Integer, Booking> bookings = new HashMap<>();

    @Override
    public void save(Booking booking) {
        ValidationUtil.checkDuplicateKeyInMap(bookings, booking.getId(), "Booking");

        bookings.put(booking.getId(), booking);
    }

    @Override
    public Optional<Booking> findById(int bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public List<Booking> findByDate(LocalDate date) {
        return bookings.values().stream()
                .filter(booking -> booking.getBookingDate().equals(date))
                .toList();
    }

    @Override
    public List<Booking> findByCustomerId(int customerId) {
        return bookings.values()
                .stream()
                .filter(booking -> booking.getCustomerId() == customerId)
                .toList();
    }

    @Override
    public List<Booking> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookings.values()
                .stream()
                .filter(booking -> !booking.getBookingDate().isBefore(startDate) && !booking.getBookingDate().isAfter(endDate))
                .toList();
    }
}