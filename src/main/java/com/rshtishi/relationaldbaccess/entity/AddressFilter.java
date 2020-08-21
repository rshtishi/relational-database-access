package com.rshtishi.relationaldbaccess.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressFilter {
	
	private String zipCode;
	private String city;
	private String state;

}
