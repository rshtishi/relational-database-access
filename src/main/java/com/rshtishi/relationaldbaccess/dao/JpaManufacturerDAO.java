package com.rshtishi.relationaldbaccess.dao;

import java.util.List;

import com.rshtishi.relationaldbaccess.entity.Manufacturer;

public interface JpaManufacturerDAO {
	
	List<Manufacturer> findAll();
	
	Manufacturer findById(int id);
	
	void save(Manufacturer manufacturer);
	
	void delete(int id);

}
