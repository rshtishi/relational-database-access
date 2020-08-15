package com.rshtishi.relationaldbaccess.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	
	private int id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String zipCode;
	private String city;
	private String state;
	
}
