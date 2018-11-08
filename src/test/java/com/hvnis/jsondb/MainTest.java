package com.hvnis.jsondb;

import com.hvnis.jsondb.entity.Customer;
import com.hvnis.jsondb.repository.CustomerRepository;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

/**
 * @author hvnis
 */
public class MainTest {
    
    @Test
    public void testRepository() throws IOException
    {
        CustomerRepository customerRepository = new CustomerRepository("src/test/data/customers-test.json");
        
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setName("Test customer name");
        customer.setPhoneNumber("Test phone number");
        customerRepository.save(customer);

        Customer customer2 = new Customer();
        customer2.setId(UUID.randomUUID().toString());
        customer2.setName("Test customer name 2");
        customer2.setPhoneNumber("Test phone number 2");
        customerRepository.save(customer2);

        Customer customerUpdate = new Customer();
        customerUpdate.setId(customer2.getId());
        customerUpdate.setName("Test customer name updated");
        customerUpdate.setPhoneNumber("Test phone number updated");
        customerRepository.update(customerUpdate);

        final List<Customer> list = customerRepository.findAll();
        list.forEach(element -> {
            try {
                customerRepository.deleteById(element.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        

    }
}
