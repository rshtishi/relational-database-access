package com.rshtishi.relationaldbaccess.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rshtishi.relationaldbaccess.entity.JpaCustomerEntity;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringDataJpaCustomerDAOTest {

	@Autowired
	private SpringDataJpaCustomerDAO customerDAO;

	@Test
	@Order(1)
	void testFindAll() {
		// setup
		// exercise
		List<JpaCustomerEntity> customers = customerDAO.findAll();
		// verify
		int expectedSize = 1;
		assertEquals(expectedSize, customers.size());
	}

	@Test
	@Order(2)
	void testFindById() {
		// setup
		int id = 1;
		// exercise
		JpaCustomerEntity customer = customerDAO.findById(id);
		// verify
		assertEquals(id, customer.getId());
	}

	@Test
	@Order(3)
	void testSave() {
		// setup
		JpaCustomerEntity newCustomer = new JpaCustomerEntity(2, "John", "Doe", "457879878", "johndoe@mail", "1411",
				"New Jork", "USA");
		// execute
		customerDAO.save(newCustomer);
		// verify
		JpaCustomerEntity customer = customerDAO.findById(newCustomer.getId());
		assertEquals(newCustomer.getId(), customer.getId());
		assertEquals(newCustomer.getFirstName(), customer.getFirstName());
	}

	@Test
	@Order(4)
	void testSaveAll() {
		// setup
		int id = 3;
		String name = "Jane";
		List<JpaCustomerEntity> customers = new ArrayList<>();
		customers.add(new JpaCustomerEntity(id, name, "Doe", "457879878", "janendoe@mail", "1411", "New Jork", "USA"));
		// execute
		customerDAO.saveAll(customers);
		// verify
		JpaCustomerEntity customer = customerDAO.findById(id);
		assertEquals(id, customer.getId());
		assertEquals(name, customer.getFirstName());
	}

	@Test
	@Order(5)
	void testDelete() {
		// setup
		int id = 3;
		JpaCustomerEntity customer = customerDAO.findById(id);
		// execute
		customerDAO.delete(customer);
		// verify
		List<JpaCustomerEntity> customers = customerDAO.findAll();
		int expectedSize = 2;
		assertEquals(expectedSize, customers.size());
	}

	@Test
	@Order(6)
	void testFindByFirstName() {
		// setup
		String name = "Rando";
		// execute
		List<JpaCustomerEntity> customers = customerDAO.findByFirstName(name);
		// verify
		int expectedSize = 1;
		assertEquals(expectedSize, customers.size());
	}

	@Test
	@Order(7)
	void testFindByEmailIsNotNull() {
		// setup
		// execute
		List<JpaCustomerEntity> customers = customerDAO.findByEmailIsNotNull();
		// verify
		int expectedSize = 2;
		assertEquals(expectedSize, customers.size());
	}

	@Test
	@Order(8)
	void testExtractCustomersThatAreFrom() {
		// setup
		String city = "Tirane";
		// execute
		List<JpaCustomerEntity> customers = customerDAO.extractCustomersThatAreFrom(city);
		// verify
		int expectedSize = 1;
		assertEquals(expectedSize, customers.size());
	}

	@Test
	@Order(9)
	void testExtractCustomerFromCountry() {
		// setup
		String country = "Albania";
		// execute
		List<JpaCustomerEntity> customers = customerDAO.extractCustomerFromCountry(country);
		// verify
		int expectedSize = 1;
		assertEquals(expectedSize, customers.size());
	}

}
