package com.davidasare.FoodOrderingSystem.repository;

import com.davidasare.FoodOrderingSystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Optional<Product> findByIdAndCategoryId(Long id, Long categoryId);
}
