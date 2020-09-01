package com.rshtishi.relationaldbaccess.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NonNull
	private String name;
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();
	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
	private ProductDetails details;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "product_manufacturer", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "manufacturer_id"))
	private List<Manufacturer> manufacturers = new ArrayList<>();

	public void addReview(Review review) {
		reviews.add(review);
		review.setProduct(this);
	}

	public void removeReview(Review review) {
		reviews.remove(review);
		review.setProduct(null);
	}

	public void setDetails(ProductDetails details) {
		this.details = details;
		details.setProduct(this);
	}

}
