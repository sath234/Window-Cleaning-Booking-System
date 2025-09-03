package uk.gov.dvla.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Booking model class.
 */
@Getter
@Setter
@EqualsAndHashCode
public class Booking {

    public Booking(int id, int customerId, LocalDate bookingDate) {
        this.id = id;
        this.customerId = customerId;
        this.bookingDate = bookingDate;
    }

    /**
     * Booking id.
     */
    private final int id;
    /**
     * Customer number.
     */
    private final int customerId;
    /**
     * Booking date.
     */
    private LocalDate bookingDate;
}
