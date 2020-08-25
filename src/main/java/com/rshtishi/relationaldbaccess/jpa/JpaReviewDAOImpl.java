package com.rshtishi.relationaldbaccess.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rshtishi.relationaldbaccess.dao.JpaReviewDAO;
import com.rshtishi.relationaldbaccess.entity.Product;
import com.rshtishi.relationaldbaccess.entity.Review;

@Repository
public class JpaReviewDAOImpl implements JpaReviewDAO{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly= true)
	@Override
	public List<Review>  findAll() {
		TypedQuery<Review> query = entityManager.createQuery("Select r from Review r",
				Review.class);
		return query.getResultList();
		
	}
	
	@Transactional
	@Override
	public void insert(Review review) {
		entityManager.persist(review);
	}

}
