package com.rshtishi.relationaldbaccess.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rshtishi.relationaldbaccess.dao.JpaProductDAO;
import com.rshtishi.relationaldbaccess.entity.Product;

@Repository
public class JpaProductDAOImpl implements JpaProductDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly = true)
	@Override
	public List<Product> findAll() {
		TypedQuery<Product> query = entityManager.createQuery("Select p from Product p",
				Product.class);
		return query.getResultList();
	}
	
	@Transactional(readOnly = true)
	@Override
	public Product findById(int id) {
		
		return entityManager.find(Product.class,id);
	}

}
