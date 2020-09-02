package com.rshtishi.relationaldbaccess.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rshtishi.relationaldbaccess.entity.Manufacturer;
import com.rshtishi.relationaldbaccess.entity.Product;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JpaManufacturerDAOImplTest {

	@Autowired
	private JpaManufacturerDAOImpl manufacturerDAO;

	@Test
	@Order(1)
	void testFindAll() {
		// setup
		// execute
		List<Manufacturer> manufacturers = manufacturerDAO.findAll();
		// verify
		int expectedSize = 2;
		assertEquals(expectedSize, manufacturers.size());
	}

	@Test
	@Order(2)
	void testFindById() {
		// setup
		int id = 1;
		// execute
		Manufacturer manufacturer = manufacturerDAO.findById(id);
		// verify
		String expectedManufacturerName = "Apple";
		assertEquals(expectedManufacturerName, manufacturer.getName());
	}

	@Test
	@Order(3)
	void testSave() {
		// setup
		Manufacturer manufacturer = new Manufacturer("Xiaomi");
		// execute
		manufacturerDAO.save(manufacturer);
		// verify
		List<Manufacturer> manufacturers = manufacturerDAO.findAll();
		int expectedSize = 3;
		assertEquals(expectedSize, manufacturers.size());
	}

	@Test
	@Order(4)
	void testDelete() {
		// setup
		int id = 3;
		// execute
		manufacturerDAO.delete(id);
		// verify
		List<Manufacturer> manufacturers = manufacturerDAO.findAll();
		int expectedSize = 2;
		assertEquals(expectedSize, manufacturers.size());
	}
	
	@Test
	@Order(5)
	void testSaveWithManyProducts() {
		// setup
		Manufacturer manufacturer = new Manufacturer("Xiaomi");
		Product product1 = new Product("Smart Watch");
		Product product2 = new Product("Smart Band");
		manufacturer.addProduct(product1);
		manufacturer.addProduct(product2);
		//execute
		manufacturerDAO.save(manufacturer);
		// verify
		List<Manufacturer> manufacturers = manufacturerDAO.findAll();
		int expectedSize = 3;
		assertEquals(expectedSize, manufacturers.size());
		
		
	}

}
