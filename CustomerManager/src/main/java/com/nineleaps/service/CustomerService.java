package com.nineleaps.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.nineleaps.model.CustomerDTO;


public interface CustomerService extends UserDetailsService {
	CustomerDTO createUser(CustomerDTO customerDTO);
	//List<CustomerDTO> findAll();

	CustomerDTO getUserDetailsName(String name);
}
