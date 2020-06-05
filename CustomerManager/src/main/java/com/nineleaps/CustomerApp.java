package com.nineleaps;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@Component
public class CustomerApp {

	private final CustomerRepository customerRepository;

	@Autowired
	public CustomerApp(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@PostConstruct
	public void generateTestData() {
		customerRepository.save(new Customer("Rocky", "Sahu", "rocky@gmail.com", "BTM", "Bangalore","12345"));
		customerRepository.save(new Customer("Bicky", "Sahu", "bicky@gmail.com", "HSR Street", "Bangalore","12345"));
	}

	/*@Bean
	ServletRegistrationBean h2servletRegistration() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
		registrationBean.addUrlMappings("/console/*");
		return registrationBean;
	}*/

	public static void main(String[] args) {
		SpringApplication.run(CustomerApp.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}
