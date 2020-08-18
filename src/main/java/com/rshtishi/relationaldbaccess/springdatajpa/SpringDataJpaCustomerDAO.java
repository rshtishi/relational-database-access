package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rshtishi.relationaldbaccess.entity.JpaCustomerEntity;

public interface SpringDataJpaCustomerDAO extends CrudRepository<JpaCustomerEntity, Integer> {

	List<JpaCustomerEntity> findAll();
	
	JpaCustomerEntity findById(int id);

}
