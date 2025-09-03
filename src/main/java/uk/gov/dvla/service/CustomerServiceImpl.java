package uk.gov.dvla.service;

import lombok.AllArgsConstructor;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.persistence.CustomerDAO;
import uk.gov.dvla.util.ValidationUtil;

@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private CustomerDAO customerDAO;

    @Override
    public void saveCustomer(Customer customer) {
        ValidationUtil.checkObjectIsNotNull(customer, "Customer");
        customerDAO.save(customer);
    }
}
