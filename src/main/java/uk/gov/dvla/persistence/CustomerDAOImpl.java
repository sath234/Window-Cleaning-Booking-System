package uk.gov.dvla.persistence;

import lombok.NoArgsConstructor;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.util.ValidationUtil;

import java.util.*;

/**
 * Implementation of CustomerDAO interface.
 * This class provides methods to manage customers in memory database.
 */
public class CustomerDAOImpl implements CustomerDAO {

    /**
     * Map of Customer representing customer in memory database.
     * The key is the customer id and the value is the Customer object.
     */
    private final Map<Integer, Customer> customers = new HashMap<>();

    @Override
    public void save(Customer customer) {
        ValidationUtil.checkDuplicateKeyInMap(customers, customer.getId(), "Customer");

        customers.put(customer.getId(), customer);
    }

    @Override
    public Optional<Customer> findById(int customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public List<Customer> findByName(String name) {
        return customers.values().stream().filter(c -> c.getName().equals(name)).toList();
    }
}