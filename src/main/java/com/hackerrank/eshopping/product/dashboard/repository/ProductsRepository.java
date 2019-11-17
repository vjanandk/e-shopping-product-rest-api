package com.hackerrank.eshopping.product.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hackerrank.eshopping.product.dashboard.model.Product;

public interface ProductsRepository extends JpaRepository<Product, Long>{
	
	@Query(value = "SELECT *  FROM PRODUCT p WHERE p.CATEGORY = :category ORDER BY p.AVAILABILITY DESC, p.DISCOUNTED_PRICE ASC, p.ID ASC", nativeQuery = true)
	List<Product> findAllByCategory(String category);
	
	@Query(value = "SELECT *  FROM PRODUCT p WHERE p.CATEGORY = :category AND p.AVAILABILITY = :availability ORDER BY (p.RETAIL_PRICE - p.DISCOUNTED_PRICE)*100/p.RETAIL_PRICE DESC, p.DISCOUNTED_PRICE ASC, p.ID ASC", nativeQuery = true)
	List<Product> findAllByCategoryAndAvailability(String category, String availability);

}
