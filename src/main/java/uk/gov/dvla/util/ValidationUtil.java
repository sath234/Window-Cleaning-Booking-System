package uk.gov.dvla.util;

import uk.gov.dvla.model.exception.BookingDateInThePastException;
import uk.gov.dvla.model.exception.DuplicateEntityException;
import uk.gov.dvla.model.exception.InvalidDateRangeException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for validation operations.
 */
public final class ValidationUtil {

    /**
     * Checks if an object is already present in the HashMap.
     *
     * @param <T>          the type of object
     * @param map          the Map to check
     * @param key          the key to check for
     * @param errorMessage the error message for the object type
     */
    public static <T> void checkDuplicateKeyInMap(Map<Integer, T> map,
                                                  int key, String errorMessage) {
        if (map.containsKey(key)) {
            throw new DuplicateEntityException("Duplicate "
                    + errorMessage + " not allowed");
        }
    }

    /**
     * Checks if an object is not null.
     *
     * @param <T>        the type of object
     * @param object     the object to check
     * @param objectName the name of the object for error message
     */
    public static <T> void checkObjectIsNotNull(final T object,
                                                final String objectName) {
        Objects.requireNonNull(object, objectName + " cannot be null");
    }

    /**
     * Checks if the date is not in the past.
     *
     * @param date the date to check
     */
    public static void checkDateNotInPast(final LocalDate date) {
        if (date != null && date.isBefore(LocalDate.now())) {
            throw new BookingDateInThePastException(
                    "Booking date cannot be in the past");
        }
    }

    /**
     * Checks if the start date is before the end date.
     *
     * @param startDate the start date
     * @param endDate   the end date
     */
    public static void checkStartDateIsBeforeEndDate(final LocalDate startDate, final LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date cannot be after end date");
        }
    }
}
