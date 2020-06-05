package com.nineleaps.web;

import java.util.ArrayList;
import java.util.List;

import com.nineleaps.Item;
import com.nineleaps.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CatalogController {

	private final ItemRepository itemRepository;

	@Autowired
	public CatalogController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}


	@GetMapping("/catalog/{id}")
	public Item Item(@PathVariable("id") long id) {
		Item item = itemRepository.findById(id).get();
		return item;
	}

	@GetMapping
	public List<Item> ItemList() {
		List<Item> itemList = new ArrayList();
		itemList = (List<Item>) itemRepository.findAll();
		return itemList;
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView add() {
		return new ModelAndView("item", "item", new Item());
	}

	@PostMapping
	public Item post(@RequestBody Item item) {
		item = itemRepository.save(item);
		return item;
	}

	@PutMapping("/{id}")
	public Item put(@PathVariable("id") long id, @RequestBody Item item) {
		item.setId(id);
		itemRepository.save(item);

		return item;
	}

	@RequestMapping(value = "/searchForm.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView searchForm() {
		return new ModelAndView("searchForm");
	}

	@GetMapping("/{name}")
	public List<Item> search(@PathVariable("name")String query) {
		List<Item> itemListByName = itemRepository.findByNameContaining(query);
		return itemListByName;
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") long id) {
		itemRepository.deleteById(id);
		return "success";
	}
}
