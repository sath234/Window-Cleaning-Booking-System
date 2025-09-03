package uk.gov.dvla.service;

import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.Customer;

import java.time.LocalDate;
import java.util.List;

/**
 * Combined service interface for window cleaning operations.
 * Extends both BookingService and CustomerService to provide
 * a unified interface for all window cleaning business operations.
 */
public interface WindowCleaningService extends BookingService, CustomerService {
}
