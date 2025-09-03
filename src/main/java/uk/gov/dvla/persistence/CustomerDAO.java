package uk.gov.dvla.persistence;

import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.DuplicateEntityException;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Customer entities.
 * Provides CRUD operations and query methods for customers.
 */
public interface CustomerDAO {
    
    /**
     * Saves a customer to the data store.
     *
     * @param customer the customer to save
     * @throws DuplicateEntityException if customer already exists
     */
    void save(Customer customer);

    /**
     * Finds a customer by their ID.
     *
     * @param customerId the customer ID
     * @return Optional containing the customer if found, empty otherwise
     */
    Optional<Customer> findById(int customerId);

    /**
     * Retrieves all customers.
     *
     * @return list of all customers
     */
    List<Customer> findAll();

    /**
     * Finds customers by name.
     *
     * @param name the name to search for
     * @return list of customers with the given name
     */
    List<Customer> findByName(String name);
}
