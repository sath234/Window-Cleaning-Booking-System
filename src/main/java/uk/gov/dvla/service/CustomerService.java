package uk.gov.dvla.service;

import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.CustomerNotFoundException;
import uk.gov.dvla.model.exception.DuplicateEntityException;

/**
 * Service interface for managing customer operations.
 */
public interface CustomerService {
    
    /**
     * Adds a new customer to the system.
     *
     * @param customer the customer to add
     * @throws RuntimeException if customer is null
     * @throws DuplicateEntityException if customer already exists
     */
    void addCustomer(Customer customer);
    
    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId the customer ID
     * @return the customer
     * @throws CustomerNotFoundException if customer not found
     */
    Customer getCustomer(int customerId);
    
    /**
     * Finds a customer ID by their name.
     *
     * @param name the customer name
     * @return the customer ID
     * @throws RuntimeException if name is null
     * @throws CustomerNotFoundException if customer not found
     */
    int getCustomerIdByName(String name);
}