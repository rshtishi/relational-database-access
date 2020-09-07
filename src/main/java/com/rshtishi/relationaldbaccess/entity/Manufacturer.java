package com.rshtishi.relationaldbaccess.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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
public class Manufacturer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NonNull
	private String name;
	@ManyToMany(mappedBy = "manufacturers", cascade = { CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.LAZY)
	private List<Product> products = new ArrayList<>();

	public void addProduct(Product product) {
		products.add(product);
		product.getManufacturers().add(this);
	}

	public void removeProduct(Product product) {
		product.getManufacturers().remove(this);
		//products.remove(product);
	}
}
