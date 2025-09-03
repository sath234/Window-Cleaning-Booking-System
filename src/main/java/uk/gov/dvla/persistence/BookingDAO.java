package uk.gov.dvla.persistence;

import uk.gov.dvla.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface BookingDAO {
    void save(Booking booking);
    Optional<Booking> findById(int bookingId);
    List<Booking> findAll();
    List<Booking> findByDate(LocalDate date);
    List<Booking> findByCustomerId(int customerId);
}
