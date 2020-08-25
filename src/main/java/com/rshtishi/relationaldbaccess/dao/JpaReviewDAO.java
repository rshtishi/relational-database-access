package com.rshtishi.relationaldbaccess.dao;

import java.util.List;

import com.rshtishi.relationaldbaccess.entity.Product;
import com.rshtishi.relationaldbaccess.entity.Review;

public interface JpaReviewDAO {
	
	List<Review> findAll();
	
	void insert(Review review);

}
