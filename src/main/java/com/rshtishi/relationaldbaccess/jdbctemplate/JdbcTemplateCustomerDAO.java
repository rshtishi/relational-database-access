package com.rshtishi.relationaldbaccess.jdbctemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rshtishi.relationaldbaccess.dao.CustomerDAO;
import com.rshtishi.relationaldbaccess.entity.Customer;

@Repository
public class JdbcTemplateCustomerDAO implements CustomerDAO {

	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public void insert(Customer customer) {
		jdbc.update(INSERT_SQL, customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getPhone(),
				customer.getEmail(), customer.getZipCode(), customer.getCity(), customer.getState());
	}

	@Override
	public void insert(Iterable<Customer> customers) {
		customers.forEach(this::insert);
	}

	@Override
	public void update(Customer customer) {
		jdbc.update(UPDATE_SQL, customer.getFirstName(), customer.getLastName(), customer.getPhone(),
				customer.getEmail(), customer.getZipCode(), customer.getCity(), customer.getState(),customer.getId());
	}

	@Override
	public void delete(Customer customer) {
		jdbc.update(DELETE_SQL, customer.getId());
	}

	@Override
	public Customer findByCustomerId(int customerId) {
		return jdbc.queryForObject(SELECT_ONE_SQL, this::mapRowToCustomer, customerId);
	}

	@Override
	public List<Customer> findAll() {
		return jdbc.query(SELECT_ALL_SQL, this::mapRowToCustomer);
	}

	private Customer mapRowToCustomer(ResultSet rs, int rownum) throws SQLException {
		return new Customer(rs.getInt("ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
				rs.getString("PHONE"), rs.getString("EMAIL"), rs.getString("ZIP_CODE"), rs.getString("CITY"),
				rs.getString("STATE"));
	}

}
