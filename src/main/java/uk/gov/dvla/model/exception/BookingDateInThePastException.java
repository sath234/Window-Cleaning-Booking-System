package uk.gov.dvla.model.exception;

public class BookingDateInThePastException extends RuntimeException {
    public BookingDateInThePastException(String message) {
        super(message);
    }
}
