package com.hvnis.jsondb.repository;

import com.hvnis.jsondb.entity.Customer;
import java.io.IOException;

/**
 * @author hvnis
 */
public class CustomerRepository extends JsonRepository<Customer, String> {

    public CustomerRepository(String jsonFilePath) throws IOException {
        super(jsonFilePath);
    }
}
