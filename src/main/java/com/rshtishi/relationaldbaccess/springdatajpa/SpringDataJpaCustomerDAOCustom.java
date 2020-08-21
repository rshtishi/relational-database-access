package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.rshtishi.relationaldbaccess.entity.AddressFilter;
import com.rshtishi.relationaldbaccess.entity.JpaCustomerEntity;

public interface SpringDataJpaCustomerDAOCustom {

	public List<JpaCustomerEntity> findCustomers(AddressFilter filter);

}
