package uk.gov.dvla.service;

import uk.gov.dvla.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    void addBooking(Booking booking);
    Booking getBookingByBookingId(int bookingNumber);
    List<Booking> getAllBooking();
    List<Booking> getAllBookingForDate(LocalDate date);
    List<Booking> getAllBookingForCustomerId(int customerId);
    List<Booking> getAllBookingForDateRange(LocalDate startDate, LocalDate endDate);
    int getTotalWindowsForDate(LocalDate date);
    int getTotalWindowsForDateRange(LocalDate startDate, LocalDate endDate);
    int getTotalCostForBooking(int bookingNumber);
    int getTotalCostForDate(LocalDate date);
    int getTotalCostForDateRange(LocalDate startDate, LocalDate endDate);
}