package com.rshtishi.relationaldbaccess.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rshtishi.relationaldbaccess.dao.JpaProductDetailsDAO;
import com.rshtishi.relationaldbaccess.entity.ProductDetails;
import com.rshtishi.relationaldbaccess.entity.Review;

@Repository
public class JpaProductDetailsDAOImpl implements JpaProductDetailsDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public List<ProductDetails> findAll() {
		TypedQuery<ProductDetails> query = entityManager.createQuery("Select pd from ProductDetails pd",
				ProductDetails.class);
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public ProductDetails findById(int id) {
		return entityManager.find(ProductDetails.class, id);
	}

	@Transactional
	@Override
	public void save(ProductDetails productDetails) {
		entityManager.persist(productDetails);
	}

	@Transactional
	@Override
	public void delete(int id) {
		ProductDetails productDetails = entityManager.find(ProductDetails.class, id);
		entityManager.remove(productDetails);
	}

}
