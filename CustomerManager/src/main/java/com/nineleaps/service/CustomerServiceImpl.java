package com.nineleaps.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nineleaps.Customer;
import com.nineleaps.CustomerRepository;
import com.nineleaps.model.CustomerDTO;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public CustomerDTO createUser(CustomerDTO customerDTO) {
		// TODO Auto-generated method stub
		customerDTO.setPassword(bCryptPasswordEncoder.encode(customerDTO.getPassword()));

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Customer customer = modelMapper.map(customerDTO, Customer.class);
		customerRepository.save(customer);
		CustomerDTO customerDto = modelMapper.map(customer, CustomerDTO.class);

		return customerDto;
	}

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByName(name);

		if (customer == null)
			throw new UsernameNotFoundException(name);

		return new User(customer.getName(), customer.getPassword(), true, true, true, true, new ArrayList<>());

	}

	@Override
	public CustomerDTO getUserDetailsName(String name) {
		Customer customer = customerRepository.findByName(name);
		
		if(customer == null) throw new UsernameNotFoundException(name);
		
		
		return new ModelMapper().map(customer, CustomerDTO.class);
	}

	

}
