package uk.gov.dvla.model.exception;

public class MultipleCustomerFoundException extends RuntimeException {
    public MultipleCustomerFoundException(String message) {
        super(message);
    }
}
