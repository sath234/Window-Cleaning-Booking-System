package uk.gov.dvla.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Customer model class.
 */
@Getter
@Setter
@EqualsAndHashCode
public class Customer {

    /**
     * Constructor for Customer class.
     *
     * @param id the customer id
     * @param name the customer name
     * @param windows the number of windows
     */
    public Customer(int id, String name, int windows) {
        this.id = id;
        this.name = name;
        this.windows = windows;
    }

    /**
     * Customer number.
     */
    private final int id;
    /**
     * Customer name.
     */
    private String name;
    /**
     * Number of windows.
     */
    private int windows;
}
