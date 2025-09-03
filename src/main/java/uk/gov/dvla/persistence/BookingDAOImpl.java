package uk.gov.dvla.persistence;

import lombok.NoArgsConstructor;
import uk.gov.dvla.model.Booking;
import uk.gov.dvla.util.ValidationUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class BookingDAOImpl implements BookingDAO {

    private final Map<Integer, Booking> bookings = new HashMap<>();
    
    @Override
    public void save(Booking booking) {
        ValidationUtil.checkObjectIsNotNull(booking, "Booking");
        ValidationUtil.checkDateNotInPast(booking.getBookingDate());
        // TODO: move two above to service
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
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Booking> findByCustomerId(int customerId) {
        return bookings.values().stream()
                .filter(booking -> booking.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }
}