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
import com.rshtishi.relationaldbaccess.entity.Review;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JpaReviewDAOImplTest {
	
	@Autowired
	private JpaReviewDAOImpl reviewDAO;
	@Autowired
	private JpaProductDAOImpl productDAO;
	
	@Test
	@Order(1)
	void testFindAll() {
		//setup
		//execute
		List<Review> reviews = reviewDAO.findAll();
		//verify
		int expectedSize = 6;
		assertEquals(expectedSize, reviews.size());
	}
	

	@Test
	@Order(2)
	void testInsert() {
		//setup
		int id = 1;
		Product product = productDAO.findById(id);
		Review review = new Review("This product was great");
		review.setProduct(product);
		//execute
		reviewDAO.insert(review);
		//verify
		List<Review> reviews = reviewDAO.findAll();
		int expectedSize = 7;
		assertEquals(expectedSize, reviews.size());
		
	}

}
