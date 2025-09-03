package uk.gov.dvla.persistence;

import uk.gov.dvla.model.Customer;
import uk.gov.dvla.util.ValidationUtil;

import java.util.*;

public class CustomerDAOImpl implements CustomerDAO {

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
        return customers.values().stream().filter(customer -> customer.getName().equals(name)).toList();
    }
}