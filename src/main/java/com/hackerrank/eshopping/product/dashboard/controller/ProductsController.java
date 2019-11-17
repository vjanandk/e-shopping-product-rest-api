package com.hackerrank.eshopping.product.dashboard.controller;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.hackerrank.eshopping.product.dashboard.model.Product;
import com.hackerrank.eshopping.product.dashboard.repository.ProductsRepository;

@RestController
@RequestMapping("/products")
public class ProductsController {

	@Autowired
	ProductsRepository productsRepo;

	@PostMapping
	public ResponseEntity addProduct(@RequestBody Product product) {
		if (productsRepo.findById(product.getId()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		productsRepo.save(product);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{product_id}")
	public ResponseEntity updateByProductId(@PathVariable("product_id") long id, @RequestBody Product inProduct) {

		if (productsRepo.findById(id).isPresent()) {
			Product product = productsRepo.getOne(id);
			product.setRetailPrice(inProduct.getRetailPrice());
			product.setDiscountedPrice(inProduct.getDiscountedPrice());
			product.setAvailability(inProduct.getAvailability());
			productsRepo.save(product);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/{product_id}")
	public ResponseEntity getProductById(@PathVariable("product_id") long id) {

		if (productsRepo.findById(id).isPresent()) {
			return ResponseEntity.ok().body(productsRepo.findById(id));
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping(params = "category")
	public ResponseEntity getProductByCategory(@RequestParam(value = "category") String category) {
		return ResponseEntity.ok().body(productsRepo.findAllByCategory(category));

	}

	@GetMapping(params = { "category", "availability" })
	public ResponseEntity getProductByCategoryAndAvailability(@RequestParam Map<String, String> requestParams) {

		String category = URLDecoder.decode(requestParams.get("category"));
		String availability = URLDecoder.decode(requestParams.get("availability"));

		System.out.println("category : " + category);
		System.out.println("availability : " + availability);
		List<Product> products = productsRepo.findAllByCategoryAndAvailability(category, availability);
		return ResponseEntity.ok().body(productsRepo.findAllByCategoryAndAvailability(category, availability));

	}

	@GetMapping
	public ResponseEntity getProducts() {
		return ResponseEntity.ok().body(productsRepo.findAll());

	}

}
