package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rshtishi.relationaldbaccess.entity.Product;

@Repository
public interface SpringDataJpaProductDAO extends CrudRepository<Product,Integer>{
	
	public List<Product> findAll();
	
	public Product findById(int id);
}
