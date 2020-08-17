package com.rshtishi.relationaldbaccess.rawjdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rshtishi.relationaldbaccess.dao.CustomerDAO;
import com.rshtishi.relationaldbaccess.entity.Customer;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RawJdbcCustomerDAOTest {

	@Autowired
	private RawJdbcCustomerDAO customerDAO;

	@Test
	@Order(1)
	void testFindAll() {
		// setup
		// execute
		List<Customer> customersReturned = customerDAO.findAll();
		// verify
		int expectedSize = 1;
		assertEquals(expectedSize, customersReturned.size());
	}

	@Test
	@Order(2)
	void testFindByCustomerNo() {
		// setup
		int id = 1;
		// execute
		Customer customerReturned = customerDAO.findByCustomerId(id);
		// verify
		String expectedFirstName = "Rando";
		String expectedLastName = "Shtishi";
		assertEquals(expectedFirstName, customerReturned.getFirstName());
		assertEquals(expectedLastName, customerReturned.getLastName());
	}

	@Test
	@Order(3)
	void testInsert() {
		// setup
		Customer newCustomer = new Customer(2, "John", "Doe", "457879878", "johndoe@mail", "1411", "New Jork", "USA");
		// execute
		customerDAO.insert(newCustomer);
		// verify
		Customer returnedCustomer = customerDAO.findByCustomerId(newCustomer.getId());
		assertEquals(newCustomer.getId(), returnedCustomer.getId());
		assertEquals(newCustomer.getFirstName(), returnedCustomer.getFirstName());

	}

	@Test
	@Order(4)
	void testInsertAll() {
		// setup
		int id = 3;
		List<Customer> customers = new ArrayList<>();
		customers.add(new Customer(id, "Jane", "Doe", "457879878", "janendoe@mail", "1411", "New Jork", "USA"));
		// execute
		customerDAO.insert(customers);
		// verify
		Customer returnedCustomer = customerDAO.findByCustomerId(id);
		assertEquals(returnedCustomer.getId(), id);

	}

	@Test
	@Order(5)
	void testDelete() {
		// setup
		int id = 3;
		Customer customer = customerDAO.findByCustomerId(id);
		// execute
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
		// execute
		customerDAO.update(customer);
		// verify
		Customer returnedCustomer = customerDAO.findByCustomerId(id);
		assertEquals(name, returnedCustomer.getFirstName());
	}

}
