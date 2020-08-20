package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rshtishi.relationaldbaccess.entity.JpaCustomerEntity;

public interface SpringDataJpaCustomerDAO extends CrudRepository<JpaCustomerEntity, Integer> {

	List<JpaCustomerEntity> findAll();

	JpaCustomerEntity findById(int id);

	// derived queries

	List<JpaCustomerEntity> findByFirstName(String firstName);

	List<JpaCustomerEntity> findByEmailIsNotNull();

	// jpql query

	@Query("SELECT c FROM Customer c WHERE c.city=?1")
	List<JpaCustomerEntity> extractCustomersThatAreFrom(String city);

	// native query

	@Query(value = "SELECT * FROM Customer c WHERE c.state=?1", nativeQuery = true)
	List<JpaCustomerEntity> extractCustomerFromCountry(String country);
}
