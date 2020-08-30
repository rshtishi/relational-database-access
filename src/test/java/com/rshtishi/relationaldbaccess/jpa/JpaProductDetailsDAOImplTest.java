package com.rshtishi.relationaldbaccess.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rshtishi.relationaldbaccess.entity.Product;
import com.rshtishi.relationaldbaccess.entity.ProductDetails;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JpaProductDetailsDAOImplTest {

	@Autowired
	private JpaProductDetailsDAOImpl productDetailsDAO;
	@Autowired
	private JpaProductDAOImpl productDAO;

	@Test
	@Order(1)
	void testFindAll() {
		// setup
		// execute
		List<ProductDetails> productDetailsList = productDetailsDAO.findAll();
		// verify
		int expectedSize = 3;
		assertEquals(expectedSize, productDetailsList.size());

	}

	@Test
	@Order(2)
	void testFindById() {
		// setup
		int id = 1;
		// execute
		ProductDetails productDetails = productDetailsDAO.findById(1);
		// verify
		String createdByExpected = "Rando";
		assertEquals(id, productDetails.getId());
		assertEquals(createdByExpected, productDetails.getCreatedBy());
	}

	@Test
	@Order(3)
	void testSave() {
		// setup
		Product product = productDAO.findById(1);
		ProductDetails productDetails = new ProductDetails("Unknown", LocalDateTime.now());
		productDetails.setProduct(product);
		// execute
		productDetailsDAO.save(productDetails);
		// verify
		List<ProductDetails> productDetailsList = productDetailsDAO.findAll();
		int expectedSize = 4;
		assertEquals(expectedSize, productDetailsList.size());
	}

	@Test
	@Order(4)
	void testDelete() {
		// setup
		int id = 1;
		// execute
		productDetailsDAO.delete(id);
		// verify
		List<ProductDetails> productDetailsList = productDetailsDAO.findAll();
		int expectedSize = 3;
		assertEquals(expectedSize, productDetailsList.size());
	}

}
