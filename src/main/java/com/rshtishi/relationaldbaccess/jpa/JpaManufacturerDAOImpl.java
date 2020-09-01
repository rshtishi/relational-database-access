package com.rshtishi.relationaldbaccess.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rshtishi.relationaldbaccess.dao.JpaManufacturerDAO;
import com.rshtishi.relationaldbaccess.entity.Manufacturer;

@Repository
public class JpaManufacturerDAOImpl implements JpaManufacturerDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public List<Manufacturer> findAll() {
		TypedQuery<Manufacturer> query = entityManager.createQuery("Select m from Manufacturer m", Manufacturer.class);
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public Manufacturer findById(int id) {
		return entityManager.find(Manufacturer.class, id);
	}

	@Transactional
	@Override
	public void save(Manufacturer manufacturer) {
		entityManager.merge(manufacturer);

	}

	@Transactional
	@Override
	public void delete(int id) {
		Manufacturer manufacturer = entityManager.find(Manufacturer.class, id);
		entityManager.remove(manufacturer);
	}

}
