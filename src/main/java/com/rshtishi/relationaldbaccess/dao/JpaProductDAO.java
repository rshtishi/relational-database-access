package com.rshtishi.relationaldbaccess.dao;

import java.util.List;

import com.rshtishi.relationaldbaccess.entity.Product;

public interface JpaProductDAO {
	
	List<Product> findAll();
	
	Product findById(int id);
	
	void save(Product product);
	
	void delete(int id);

}
