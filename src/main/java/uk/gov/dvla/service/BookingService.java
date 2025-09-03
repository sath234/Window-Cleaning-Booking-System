package uk.gov.dvla.service;

import uk.gov.dvla.model.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {
//    void addBooking(Booking booking);
//    List<Booking> retrieveBookingByBookingId(int BookingNumber);
//    List<Booking> retrieveAllBooking();
//    List<Booking> retrieveAllBookingForDate(LocalDate date);
//    List<Booking> retrieveAllBookingForCustomerId(int customerId);
//    List<Booking> retrieveAllBookingForDateRange(LocalDate startDate, LocalDate endDate);
    int getTotalWindowsForDate(LocalDate date);
    int getTotalCostForBooking(int bookingNumber);
}
