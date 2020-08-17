package com.rshtishi.relationaldbaccess.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rshtishi.relationaldbaccess.dao.JpaCustomerDAO;
import com.rshtishi.relationaldbaccess.entity.JpaCustomerEntity;

@Repository
public class JpaCustomerDAOImp implements JpaCustomerDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public List<JpaCustomerEntity> findAll() {
		TypedQuery<JpaCustomerEntity> query = entityManager.createQuery("Select c from Customer c",
				JpaCustomerEntity.class);
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public JpaCustomerEntity findById(int id) {
		return entityManager.find(JpaCustomerEntity.class, id);
	}

	@Transactional
	@Override
	public void save(JpaCustomerEntity customer) {
		entityManager.merge(customer);
	}

	@Transactional
	@Override
	public void delete(int id) {
		JpaCustomerEntity customer = entityManager.find(JpaCustomerEntity.class, id);
		entityManager.remove(customer);
	}

}
