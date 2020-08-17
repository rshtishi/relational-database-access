package com.rshtishi.relationaldbaccess.dao;

import java.util.List;

import com.rshtishi.relationaldbaccess.entity.JpaCustomerEntity;

public interface JpaCustomerDAO {
	
	List<JpaCustomerEntity> findAll();
	
	JpaCustomerEntity findById(int id);
	
	void save(JpaCustomerEntity customer);
	
	void saveAll(List<JpaCustomerEntity> customer);
	
	void delete(int id);
	

}
