package com.rshtishi.relationaldbaccess.jdbctemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rshtishi.relationaldbaccess.entity.Customer;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcTemplateCustomerDAOTest {

	@Autowired
	private JdbcTemplateCustomerDAO customerDAO;

	@Test
	@Order(1)
	void testFindAll() {
		// setup
		// exercise
		List<Customer> customers = customerDAO.findAll();
		// verify
		int expectedSize = 1;
		assertEquals(expectedSize, customers.size());
	}

	@Test
	@Order(2)
	void testFindById() {
		// setup
		int customerId = 1;
		// exercise
		Customer customer = customerDAO.findByCustomerId(customerId);
		// verify
		assertEquals(customerId, customer.getId());
	}

	@Test
	@Order(3)
	void testInsert() {
		// setup
		int customerId = 2;
		Customer newCustomer = new Customer(customerId, "John", "Doe", "457879878", "johndoe@mail", "1411", "New Jork",
				"USA");
		// exercise
		customerDAO.insert(newCustomer);
		// verify
		Customer customer = this.customerDAO.findByCustomerId(customerId);
		assertEquals(customerId, customer.getId());
	}

	@Test
	@Order(4)
	void testInsertAll() {
		// setup
		int id = 3;
		List<Customer> customers = new ArrayList<>();
		customers.add(new Customer(id, "Jane", "Doe", "457879878", "janendoe@mail", "1411", "New Jork", "USA"));
		// exercise
		customerDAO.insert(customers);
		// verify
		Customer customer = customerDAO.findByCustomerId(id);
		assertEquals(id, customer.getId());
	}

	@Test
	@Order(5)
	void testDelete() {
		// setup
		int id = 3;
		Customer customer = customerDAO.findByCustomerId(id);
		// exercise
		customerDAO.delete(customer);
		// verify
		int expectedSize = 2;
		List<Customer> customers = customerDAO.findAll();
		assertEquals(expectedSize, customers.size());
	}

	@Test
	@Order(6)
	void testUpdate() {
		// setup
		int id = 2;
		String name = "Jim";
		Customer customer = new Customer(id, name, "Doe", "457879878", "johndoe@mail", "1411", "New Jork", "USA");
		// exercise
		customerDAO.update(customer);
		// verify
		Customer returnedCustomer = customerDAO.findByCustomerId(id);
		assertEquals(id, returnedCustomer.getId());
		assertEquals(name,returnedCustomer.getFirstName());
	}

}
