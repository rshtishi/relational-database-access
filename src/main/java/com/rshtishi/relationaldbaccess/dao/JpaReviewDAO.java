package com.rshtishi.relationaldbaccess.dao;

import java.util.List;

import com.rshtishi.relationaldbaccess.entity.Product;
import com.rshtishi.relationaldbaccess.entity.Review;

public interface JpaReviewDAO {
	
	List<Review> findAll();
	
	Review findById(int id);
	
	void save(Review review);
	
	void delete(int id);

}
