package com.rshtishi.relationaldbaccess.rawjdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rshtishi.relationaldbaccess.dao.CustomerDAO;
import com.rshtishi.relationaldbaccess.entity.Customer;

@Repository
public class RawJdbcCustomerDAO implements CustomerDAO {

	@Autowired
	private DataSource dataSource;

	@Override
	public void insert(Customer customer) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(INSERT_SQL)) {
			prepareInsertStatement(preparedStmt, customer);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void insert(Iterable<Customer> customers) {
		customers.forEach(this::insert);
	}

	@Override
	public void update(Customer customer) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(UPDATE_SQL)) {
			prepareUpdateStatement(preparedStmt, customer);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void delete(Customer customer) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(DELETE_SQL)) {
			preparedStmt.setInt(1, customer.getId());
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Customer findByCustomerId(int customerId) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(SELECT_ONE_SQL)) {
			preparedStmt.setInt(1, customerId);
			ResultSet rs = preparedStmt.executeQuery();
			rs.next();
			return toCustomer(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Customer> findAll() {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(SELECT_ALL_SQL)) {
			List<Customer> customers = new ArrayList<>();
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				customers.add(toCustomer(rs));
			}
			return customers;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void prepareInsertStatement(PreparedStatement preparedStmt, Customer customer) throws SQLException {
		preparedStmt.setInt(1, customer.getId());
		preparedStmt.setString(2, customer.getFirstName());
		preparedStmt.setString(3, customer.getLastName());
		preparedStmt.setString(4, customer.getPhone());
		preparedStmt.setString(5, customer.getEmail());
		preparedStmt.setString(6, customer.getZipCode());
		preparedStmt.setString(7, customer.getCity());
		preparedStmt.setString(8, customer.getState());
	}

	private void prepareUpdateStatement(PreparedStatement preparedStmt, Customer customer) throws SQLException {
		preparedStmt.setString(1, customer.getFirstName());
		preparedStmt.setString(2, customer.getLastName());
		preparedStmt.setString(3, customer.getPhone());
		preparedStmt.setString(4, customer.getEmail());
		preparedStmt.setString(5, customer.getZipCode());
		preparedStmt.setString(6, customer.getCity());
		preparedStmt.setString(7, customer.getState());
		preparedStmt.setInt(8, customer.getId());
	}

	private Customer toCustomer(ResultSet rs) throws SQLException {
		return new Customer(rs.getInt("ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
				rs.getString("PHONE"), rs.getString("EMAIL"), rs.getString("ZIP_CODE"), rs.getString("CITY"),
				rs.getString("STATE"));
	}

}
