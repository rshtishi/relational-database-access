package com.rshtishi.relationaldbaccess.rawjdbc;

import java.util.List;

public interface CustomerDAO {

	void insert(Customer customer);

	void insert(Iterable<Customer> customers);

	void update(Customer customer);

	void delete(Customer customer);

	Customer findByCustomerId(int customerId);

	List<Customer> findAll();
}
