package com.nineleaps.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nineleaps.Customer;
import com.nineleaps.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

	private CustomerRepository customerRepository;

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

	/*
	 * @RequestMapping(value = "/form.html", method = RequestMethod.GET) public
	 * ModelAndView add() { return new ModelAndView("customer", "customer", new
	 * Customer()); }
	 */

	@PostMapping
	public Customer addCustomer(@RequestBody Customer customer, HttpServletRequest httpRequest) {
		customer = customerRepository.save(customer);
		return customer;
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
