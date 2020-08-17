package com.rshtishi.relationaldbaccess.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="customer")
@Entity(name="Customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JpaCustomerEntity  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String zipCode;
	private String city;
	private String state;

}
