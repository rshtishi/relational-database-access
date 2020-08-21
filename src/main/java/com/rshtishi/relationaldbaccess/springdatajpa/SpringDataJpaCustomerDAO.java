package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.rshtishi.relationaldbaccess.entity.JpaCustomerEntity;

@Repository
public interface SpringDataJpaCustomerDAO extends CrudRepository<JpaCustomerEntity, Integer>,
		QuerydslPredicateExecutor<JpaCustomerEntity>, SpringDataJpaCustomerDAOCustom {

	List<JpaCustomerEntity> findAll();

	List<JpaCustomerEntity> findAll(Predicate predicate);

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
