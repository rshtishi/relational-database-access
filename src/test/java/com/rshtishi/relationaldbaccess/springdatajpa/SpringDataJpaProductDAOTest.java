package com.rshtishi.relationaldbaccess.springdatajpa;

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
class SpringDataJpaProductDAOTest {

	@Autowired
	private SpringDataJpaProductDAO productDAO;

	@Test
	@Order(1)
	void testFindAll() {
		// setup
		// execute
		List<Product> products = productDAO.findAll();
		// verify
		int expectedSize = 3;
		assertEquals(expectedSize, products.size());
	}

	@Test
	@Order(2)
	void testFindById() {
		// setup
		int id = 1;
		// execute
		Product product = productDAO.findById(id);
		// verify
		String expectedProductName = "Lap Top";
		assertEquals(expectedProductName, product.getName());
	}

	@Test
	@Order(3)
	void testSave() {
		// setup
		Product product = new Product("PC");
		// execute
		productDAO.save(product);
		// verify
		int expectedSize = 4;
		List<Product> products = productDAO.findAll();
		assertEquals(expectedSize, products.size());
	}

	@Test
	@Order(4)
	void testDeleteById() {
		// setup
		int id = 1;
		// execute
		productDAO.deleteById(id);
		// verify
		int expectedSize = 3;
		List<Product> products = productDAO.findAll();
		assertEquals(expectedSize, products.size());
	}

	@Test
	@Order(5)
	void testSaveManyProductWithManyManufacturer() {
		// setup
		Product product = new Product("Tablet");
		Manufacturer oppoManufacturer = new Manufacturer("Oppo");
		Manufacturer googleManufacturer = new Manufacturer("Google");
		product.getManufacturers().add(oppoManufacturer);
		product.getManufacturers().add(googleManufacturer);
		// execute
		productDAO.save(product);
		// verify
		int expectedSize = 4;
		List<Product> products = productDAO.findAll();
		assertEquals(expectedSize, products.size());
	}

}
