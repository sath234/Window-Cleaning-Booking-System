package uk.gov.dvla.service;

import lombok.AllArgsConstructor;
import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.BookingNotFoundException;
import uk.gov.dvla.model.exception.CustomerNotFoundException;
import uk.gov.dvla.model.exception.MultipleCustomerFoundException;
import uk.gov.dvla.persistence.BookingDAO;
import uk.gov.dvla.persistence.CustomerDAO;
import uk.gov.dvla.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
public class WindowCleaningServiceImpl implements WindowCleaningService {

    public static final int COST_PER_PROPERTY = 5;
    public static final int COST_PER_WINDOW = 1;
    private final BookingDAO bookingDAO;
    private final CustomerDAO customerDAO;

    @Override
    public void addCustomer(Customer customer) {
        ValidationUtil.checkObjectIsNotNull(customer, "Customer");
        customerDAO.save(customer);
    }

    @Override
    public Customer getCustomer(int customerId) {
        return customerDAO.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found")
        );
    }

    @Override
    public int getCustomerIdByName(String name) {
        ValidationUtil.checkObjectIsNotNull(name, "String");

        List<Customer> customers = customerDAO.findByName(name);

        if (customers.isEmpty()) {
            throw new CustomerNotFoundException("No customer found");
        }

        if (customers.size() > 1) {
            throw new MultipleCustomerFoundException("Multiple customers found");
        }

        return customers.get(0).getId();
    }

    @Override
    public void addBooking(Booking booking) {
        ValidationUtil.checkObjectIsNotNull(booking, "booking");
        ValidationUtil.checkDateNotInPast(booking.getBookingDate());

        // Check the customer exists
        customerDAO.findById(booking.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("No customer found"));

        bookingDAO.save(booking);
    }

    @Override
    public Booking getBookingByBookingId(int bookingNumber) {
        return bookingDAO.findById(bookingNumber)
                .orElseThrow(() -> new BookingNotFoundException("No booking found"));
    }

    @Override
    public List<Booking> getAllBooking() {
        return bookingDAO.findAll();
    }

    @Override
    public List<Booking> getAllBookingForDate(LocalDate date) {
        ValidationUtil.checkObjectIsNotNull(date, "LocalDate");

        return bookingDAO.findByDate(date);
    }

    @Override
    public List<Booking> getAllBookingForCustomerId(int customerId) {
        return bookingDAO.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getAllBookingForDateRange(LocalDate startDate, LocalDate endDate) {
        ValidationUtil.checkObjectIsNotNull(startDate, "startDate");
        ValidationUtil.checkObjectIsNotNull(endDate, "endDate");

        return bookingDAO.findByDateRange(startDate, endDate);
    }

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
    public int getTotalWindowsForDateRange(LocalDate startDate, LocalDate endDate) {
        ValidationUtil.checkObjectIsNotNull(startDate, "startDate");
        ValidationUtil.checkObjectIsNotNull(endDate, "endDate");
        // TODO: Validation that start and end are right way round

        List<Booking> bookings = bookingDAO.findByDateRange(startDate, endDate);

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

    @Override
    public int getTotalCostForDate(LocalDate date) {
        ValidationUtil.checkObjectIsNotNull(date, "LocalDate");

        List<Booking> bookings = bookingDAO.findByDate(date);

        return bookings.stream()
                .mapToInt(b -> {
                    Customer customer = customerDAO.findById(b.getCustomerId())
                            .orElseThrow(() -> new CustomerNotFoundException("No customer found"));

                    return (customer.getWindows() * COST_PER_WINDOW) + COST_PER_PROPERTY;
                })
                .sum();
    }

    @Override
    public int getTotalCostForDateRange(LocalDate startDate, LocalDate endDate) {
        ValidationUtil.checkObjectIsNotNull(startDate, "startDate");
        ValidationUtil.checkObjectIsNotNull(endDate, "endDate");
        // TODO: Validation that start and end are right way round

        List<Booking> bookings = bookingDAO.findByDateRange(startDate, endDate);

        return bookings.stream()
                .mapToInt(b -> {
                    Customer customer = customerDAO.findById(b.getCustomerId())
                            .orElseThrow(() -> new CustomerNotFoundException("No customer found"));

                    return (customer.getWindows() * COST_PER_WINDOW) + COST_PER_PROPERTY;
                })
                .sum();
    }
}
