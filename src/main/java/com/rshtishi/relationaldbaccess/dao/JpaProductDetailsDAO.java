package com.rshtishi.relationaldbaccess.dao;

import java.util.List;

import com.rshtishi.relationaldbaccess.entity.ProductDetails;

public interface JpaProductDetailsDAO {
	
	List<ProductDetails> findAll();
	
	ProductDetails findById(int id);
	
	void save(ProductDetails productDetails);
	
	void delete(int id);
	
}
