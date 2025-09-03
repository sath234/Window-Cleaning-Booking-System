package uk.gov.dvla.service;

import lombok.AllArgsConstructor;
import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.BookingNotFoundException;
import uk.gov.dvla.model.exception.CustomerNotFoundException;
import uk.gov.dvla.persistence.BookingDAO;
import uk.gov.dvla.persistence.CustomerDAO;
import uk.gov.dvla.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    public static final int COST_PER_PROPERTY = 5;
    public static final int COST_PER_WINDOW = 1;
    private final BookingDAO bookingDAO;
    private final CustomerDAO customerDAO;

    @Override
    public int getTotalWindowsForDate(LocalDate date) {
        ValidationUtil.checkObjectIsNotNull(date, "LocalDate");

        List<Booking> bookings = bookingDAO.findByDate(date);

        return bookings.stream()
                .mapToInt(b -> {
                    Customer customer = customerDAO.findById(b.getCustomerId())
                            .orElseThrow(() -> new CustomerNotFoundException("No customer found"));

                    return customer.getWindows();
                })
                .sum();
    }

    @Override
    public int getTotalCostForBooking(int bookingNumber) {
        Booking booking = bookingDAO.findById(bookingNumber)
                .orElseThrow(() -> new BookingNotFoundException("No booking found"));

        Customer customer = customerDAO.findById(booking.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("No customer found"));



        return (customer.getWindows() * COST_PER_WINDOW) + COST_PER_PROPERTY;
    }
}
