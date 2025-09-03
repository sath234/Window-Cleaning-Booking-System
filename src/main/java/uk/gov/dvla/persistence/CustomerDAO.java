package uk.gov.dvla.persistence;

import uk.gov.dvla.model.Customer;

import java.util.List;
import java.util.Optional;


public interface CustomerDAO {
    void save(Customer customer);
    Optional<Customer> findById(int customerId);
    List<Customer> findAll();
    List<Customer> findByName(String name);
}
