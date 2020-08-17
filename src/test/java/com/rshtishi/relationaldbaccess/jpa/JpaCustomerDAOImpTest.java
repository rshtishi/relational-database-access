package com.rshtishi.relationaldbaccess.jpa;

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
class JpaCustomerDAOImpTest {
	
	@Autowired
	private JpaCustomerDAOImp customerDAO;

	@Test
	@Order(1)
	void testFindAll() {
		//setup
		//execute
		List<JpaCustomerEntity> customers = customerDAO.findAll();
		//verify
		int expectedSize = 1;
		assertEquals(expectedSize, customers.size());
	}
	
	@Test
	@Order(2)
	void testFindById() {
		// setup
		int id = 1;
		// execute
		JpaCustomerEntity customer = customerDAO.findById(id);
		//verify
		String expectedName = "Rando";
		assertEquals(id,customer.getId());
		assertEquals(expectedName,customer.getFirstName());
	}
	
	@Test
	@Order(3)
	void testSave() {
		// setup
		JpaCustomerEntity  newCustomer = new JpaCustomerEntity(2, "John", "Doe", "457879878", "johndoe@mail", "1411", "New Jork", "USA");
		// execute
		customerDAO.save(newCustomer);
		//verify
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
		//execute
		customerDAO.saveAll(customers);
		//verify
		JpaCustomerEntity customer = customerDAO.findById(id);
		assertEquals(id, customer.getId());
		assertEquals(name, customer.getFirstName());
	}
	
	@Test
	@Order(5)
	void testDelete() {
		//setup 
		int id = 3;
		//execute
		customerDAO.delete(id);
		//verify
		List<JpaCustomerEntity> customers = customerDAO.findAll();
		int expectedSize = 2;
		assertEquals(expectedSize, customers.size());
	}

}
