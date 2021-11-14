package com.davidasare.FoodOrderingSystem.controllers;


import com.davidasare.FoodOrderingSystem.exception.ResourceNotFoundException;
import com.davidasare.FoodOrderingSystem.model.Category;
import com.davidasare.FoodOrderingSystem.model.Product;
import com.davidasare.FoodOrderingSystem.repository.CategoryRepository;
import com.davidasare.FoodOrderingSystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

//    @GetMapping("/category")
//    public List<Product> getProducts(){
//        return productRepository.findAll();
//    }

    @GetMapping("/{categoryId}/products")
    public Page<Product> getAllProductsByCategoryId(@PathVariable (value = "categoryId") Long categoryId,
                                                   Pageable pageable){
        return productRepository.findByCategoryId(categoryId, pageable);
    }

//    @PostMapping
//    public ResponseEntity createProduct(@RequestBody Product product) throws URISyntaxException {
//        Product savedProduct = productRepository.save(product);
//        return ResponseEntity.created(new URI("/product/" + savedProduct.getId())).body(savedProduct);
//    }
    @PostMapping("/{categoryId}/products")
    public Product createProduct(@PathVariable (value = "categoryId") Long categoryId,
                                 @Valid @RequestBody Product product) {
        return categoryRepository.findById(categoryId).map(category -> {
            product.setCategory(category);
            return productRepository.save(product);
        }).orElseThrow(() -> new ResourceNotFoundException("categoryId " + categoryId + " not found"));
    }

//    @PostMapping("/category/{categoryId}/products")
//    public ResponseEntity createProduct(@PathVariable (value = "categoryId") Long postId,
//                                 @Valid @RequestBody Product product) {
//        return categoryRepository.findById(postId).map(category -> {
//            product.setCategory(category);
//            Product savedProduct = productRepository.save(product);}).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
//            return ResponseEntity.created(new URI("/products/" + savedProduct.getId())).body(savedProduct);
//       // }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
//    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody Product product) {
//        Product currentProduct = productRepository.findById(id).orElseThrow(RuntimeException::new);
//        currentProduct.setProductName(product.getProductName());
//        productRepository.save(currentProduct);
//        return ResponseEntity.ok(currentProduct);
//    }

    @PutMapping("/{categoryId}/updateProduct/{productId}")
    public Product updateProduct(@PathVariable (value = "categoryId") Long categoryId,
                                 @PathVariable (value = "productId") Long productId,
                                 @Valid @RequestBody Product productRequest) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("categoryId " + categoryId + " not found");
        }

        return productRepository.findById(productId).map(product -> {
            product.setProductName(productRequest.getProductName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            return productRepository.save(product);
        }).orElseThrow(() -> new ResourceNotFoundException("ProductId " + productId + "not found"));
    }

//    @DeleteMapping("delete/{id}")
//    public ResponseEntity deleteProduct(@PathVariable Long id){
//        productRepository.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
    @DeleteMapping("/{categoryId}/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable (value = "categoryId") Long categoryId,
                                           @PathVariable (value = "productId") Long productId) {
        return productRepository.findByIdAndCategoryId(productId, categoryId).map(product -> {
            productRepository.delete(product);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + productId + " and categoryId " + categoryId));
    }

}
