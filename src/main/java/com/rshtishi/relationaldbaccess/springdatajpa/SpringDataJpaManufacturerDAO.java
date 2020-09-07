package com.rshtishi.relationaldbaccess.springdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rshtishi.relationaldbaccess.entity.Manufacturer;

@Repository
public interface SpringDataJpaManufacturerDAO extends CrudRepository<Manufacturer, Integer> {
	
	public List<Manufacturer> findAll();
	
	public Manufacturer findById(int id);

}
