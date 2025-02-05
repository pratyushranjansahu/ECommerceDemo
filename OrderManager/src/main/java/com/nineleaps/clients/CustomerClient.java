package com.nineleaps.clients;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomerClient {

	private final Logger log = LoggerFactory.getLogger(CustomerClient.class);

	private RestTemplate restTemplate;
	private String customerServiceHost;
	private long customerServicePort;
	private boolean useRibbon;
	private LoadBalancerClient loadBalancer;

	static class CustomerPagedResources extends PagedModel<Customer> {

	}

	@Autowired
	public CustomerClient(@Value("${customer.service.host:customer}") String customerServiceHost,
			@Value("${customer.service.port:8080}") long customerServicePort,
			@Value("${ribbon.eureka.enabled:false}") boolean useRibbon) {
		super();
		this.restTemplate = getRestTemplate();
		this.customerServiceHost = customerServiceHost;
		this.customerServicePort = customerServicePort;
		this.useRibbon = useRibbon;
	}

	@Autowired(required = false)
	public void setLoadBalancer(LoadBalancerClient loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	public boolean isValidCustomerId(long customerId) {
		RestTemplate restTemplate = new RestTemplate();
		try {
			ResponseEntity<String> entity = restTemplate.getForEntity(customerURL() + customerId, String.class);
			return entity.getStatusCode().is2xxSuccessful();
		} catch (final HttpClientErrorException e) {
			if (e.getStatusCode().value() == 404)
				return false;
			else
				throw e;
		}
	}

	protected RestTemplate getRestTemplate() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
		converter.setObjectMapper(mapper);

		return new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));
	}

	public Collection<Customer> findAll() {
		PagedModel<Customer> pagedResources = null;
		 List<Customer> customerList = null;
		try {
			RestTemplate rest = getRestTemplate();
			//pagedResources = getRestTemplate().getForObject(customerURL(), CustomerPagedResources.class);

	        ResponseEntity<List<Customer>> albumsListResponse = restTemplate.exchange(customerURL(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>>() {
	        });
	        customerList=albumsListResponse.getBody(); 
	        
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		return customerList;
	}

	private String customerURL() {
		String url;
		if (useRibbon) {
			ServiceInstance instance = loadBalancer.choose("customermanager");
			url = String.format("http://%s:%s/", instance.getHost(), instance.getPort());
		} else {
			url = String.format("http://%s:%s/", customerServiceHost, customerServicePort);
		}
		log.trace("Customer: URL {} ", url);
		return url;

	}

	public Customer getOne(long customerId) {
		return restTemplate.getForObject(customerURL() + customerId, Customer.class);
	}
}
