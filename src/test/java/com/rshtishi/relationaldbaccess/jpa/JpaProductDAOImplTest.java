package com.rshtishi.relationaldbaccess.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rshtishi.relationaldbaccess.entity.Product;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JpaProductDAOImplTest {
	
	@Autowired
	private JpaProductDAOImpl productDAO;

	@Test
	@Order(1)
	void testFindAll() {
		//setup
		//execute
		List<Product> products = productDAO.findAll();
		//verify
		int expectedSize = 3;
		assertEquals(expectedSize, products.size());
	}
	
	@Test
	@Order(2)
	void testFindById() {
		//setup
		int id = 1;
		//execute
		Product product = productDAO.findById(id);
		//verify
		String expectedName = "Lap Top";
		assertEquals(expectedName, product.getName());
	}

}
