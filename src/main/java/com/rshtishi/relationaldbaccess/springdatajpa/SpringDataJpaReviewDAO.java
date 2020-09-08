package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rshtishi.relationaldbaccess.entity.Review;

public interface SpringDataJpaReviewDAO extends CrudRepository<Review, Integer> {
	
	public List<Review> findAll();
	
	public Review findById(int id);

}
