package com.nineleaps.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.Customer;
import com.nineleaps.CustomerRepository;
import com.nineleaps.model.CustomerDTO;
import com.nineleaps.model.CustomerRequestModel;
import com.nineleaps.model.CustomerResponseModel;
import com.nineleaps.model.LoginRequestModel;
import com.nineleaps.service.CustomerService;

@RestController
public class CustomerController {

	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@GetMapping("/{id}")
	public Customer customer(@PathVariable("id") long id) {
		Customer customer = customerRepository.findById(id).get();
		return customer;
	}

	@GetMapping
	public List<Customer> customerList() {
		List<Customer> customerList = new ArrayList<>();
		customerList = (List<Customer>) customerRepository.findAll();
		return customerList;
	}

	@PostMapping
	public CustomerResponseModel addCustomer(@RequestBody CustomerRequestModel customerRequestModel, HttpServletRequest httpRequest) {
		ModelMapper modelMapper = new ModelMapper(); 
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		CustomerDTO customerDto = modelMapper.map(customerRequestModel, CustomerDTO.class);
		
		CustomerDTO createdUser = customerService.createUser(customerDto);

		CustomerResponseModel customerResponseModel = modelMapper.map(createdUser, CustomerResponseModel.class);
		return customerResponseModel;
	}
	
	

	@PutMapping("/{id}")
	public Customer put(@PathVariable("id") long id, @RequestBody Customer customer, HttpServletRequest httpRequest) {
		customer.setId(id);
		customerRepository.save(customer);
		return customer;
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") long id) {
		customerRepository.deleteById(id);
		return "success";
	}
}
