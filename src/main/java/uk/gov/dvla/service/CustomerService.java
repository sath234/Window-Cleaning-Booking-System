package uk.gov.dvla.service;

import uk.gov.dvla.model.Customer;

public interface CustomerService {
    void addCustomer(Customer customer);
    Customer getCustomer(int customerId);
    int getCustomerIdByName(String name);
}