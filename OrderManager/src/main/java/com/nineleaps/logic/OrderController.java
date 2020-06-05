package com.nineleaps.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.nineleaps.clients.CatalogClient;
import com.nineleaps.clients.Customer;
import com.nineleaps.clients.CustomerClient;
import com.nineleaps.clients.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
class OrderController {

	private OrderRepository orderRepository;

	private OrderService orderService;

	private CustomerClient customerClient;
	private CatalogClient catalogClient;

	@Autowired
	private OrderController(OrderService orderService,
			OrderRepository orderRepository, CustomerClient customerClient,
			CatalogClient catalogClient) {
		super();
		this.orderRepository = orderRepository;
		this.customerClient = customerClient;
		this.catalogClient = catalogClient;
		this.orderService = orderService;
	}

	

	@GetMapping("/orderlist")
	public List<Order> orderList() {
		List<Order> orderList=new ArrayList();
		orderList.addAll((Collection<? extends Order>) orderRepository.findAll());
		return orderList;
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView form() {
		
		return new ModelAndView("orderForm", "order", new Order());
	}

	
	@GetMapping("/{id}")
	public Order get(@PathVariable("id") long id) {
		return orderRepository.findById(id).get();
	}

	
	@PostMapping
	public String post(@RequestBody Order order) {
		order = orderService.order(order);
		Double price = orderService.getPrice(order.getId());
		return "You cart value is Rs."+price;
	}

	@DeleteMapping("/{id}")
	public String post(@PathVariable("id") long id) {
		orderRepository.deleteById(id);
		

		return "Order deleted";
	}
}
