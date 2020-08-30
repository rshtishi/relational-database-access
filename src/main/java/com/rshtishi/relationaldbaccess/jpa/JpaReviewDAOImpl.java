package com.rshtishi.relationaldbaccess.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rshtishi.relationaldbaccess.dao.JpaReviewDAO;
import com.rshtishi.relationaldbaccess.entity.Review;

@Repository
public class JpaReviewDAOImpl implements JpaReviewDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public List<Review> findAll() {
		TypedQuery<Review> query = entityManager.createQuery("Select r from Review r", Review.class);
		return query.getResultList();

	}

	@Transactional(readOnly = true)
	@Override
	public Review findById(int id) {
		return entityManager.find(Review.class, 1);
	}

	@Transactional
	@Override
	public void save(Review review) {
		entityManager.merge(review);
	}
	
	@Transactional
	@Override
	public void delete(int id) {
		Review review = entityManager.find(Review.class, 1);
		entityManager.remove(review);
	}

}
