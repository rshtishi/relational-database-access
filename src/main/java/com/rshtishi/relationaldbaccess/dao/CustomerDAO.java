package com.rshtishi.relationaldbaccess.dao;

import java.util.List;

import com.rshtishi.relationaldbaccess.entity.Customer;

public interface CustomerDAO {
	
	public static final String INSERT_SQL = "INSERT INTO CUSTOMER (ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, ZIP_CODE, CITY, STATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_SQL = "UPDATE CUSTOMER SET FIRST_NAME=?, LAST_NAME=?, PHONE=?, EMAIL=?, ZIP_CODE=?, CITY=?, STATE=? WHERE ID=?";
	public static final String SELECT_ALL_SQL = "SELECT * FROM CUSTOMER";
	public static final String SELECT_ONE_SQL = "SELECT * FROM  CUSTOMER WHERE ID=?";
	public static final String DELETE_SQL = "DELETE FROM CUSTOMER WHERE ID = ?";


	void insert(Customer customer);

	void insert(Iterable<Customer> customers);

	void update(Customer customer);

	void delete(Customer customer);

	Customer findByCustomerId(int customerId);

	List<Customer> findAll();
}
