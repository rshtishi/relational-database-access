package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rshtishi.relationaldbaccess.entity.ProductDetails;

public interface SpringDataJpaProductDetailsDAO extends CrudRepository<ProductDetails, Integer> {
	
	public List<ProductDetails> findAll();
	
	public ProductDetails findById(int id);

}
