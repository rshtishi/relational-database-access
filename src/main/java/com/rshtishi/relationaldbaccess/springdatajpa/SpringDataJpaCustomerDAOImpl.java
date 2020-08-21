package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.rshtishi.relationaldbaccess.entity.AddressFilter;
import com.rshtishi.relationaldbaccess.entity.JpaCustomerEntity;
import com.rshtishi.relationaldbaccess.entity.QJpaCustomerEntity;

public class SpringDataJpaCustomerDAOImpl implements SpringDataJpaCustomerDAOCustom {

	private static final Logger logger = LoggerFactory.getLogger(SpringDataJpaCustomerDAOImpl.class);

	@Autowired
	private SpringDataJpaCustomerDAO customerDAO;

	@Override
	public List<JpaCustomerEntity> findCustomers(AddressFilter filter) {

		BooleanBuilder builder = new BooleanBuilder();
		if (filter != null) {
			QJpaCustomerEntity qCustomer = QJpaCustomerEntity.jpaCustomerEntity;
			if (!StringUtils.isBlank(filter.getZipCode())) {
				builder.and(qCustomer.zipCode.equalsIgnoreCase(filter.getZipCode()));
			}
			if (!StringUtils.isBlank(filter.getCity())) {
				builder.and(qCustomer.city.equalsIgnoreCase(filter.getCity()));
			}
			if (!StringUtils.isBlank(filter.getState())) {
				builder.and(qCustomer.state.equalsIgnoreCase(filter.getState()));
			}
		}
		return customerDAO.findAll(builder);
	}

}
