package com.davidasare.FoodOrderingSystem.controllers;


import com.davidasare.FoodOrderingSystem.model.Category;
import com.davidasare.FoodOrderingSystem.payload.request.ProductRequest;
import com.davidasare.FoodOrderingSystem.payload.response.ApiResponse;
import com.davidasare.FoodOrderingSystem.services.ProductService;
import com.davidasare.FoodOrderingSystem.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/products")
    public List<ProductRequest> getProducts(@RequestParam(required = false) boolean New, @RequestParam(required = false) String category)  {

        return (New) ? productService.listProducts().stream().sorted(Comparator.comparing(ProductRequest::getId)).limit(5).collect(Collectors.toList()) : productService.listProducts();
    }
//    @GetMapping("/Search")
//    public Category getCategoryByName(@RequestParam String category){
//        return categoryService.readCategory(category);
//    }

    @PostMapping("/product")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductRequest productRequest) {
        Optional<Category> optionalCategory = categoryService.readCategory(productRequest.getCategoryId());
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
        }
        Category category = optionalCategory.get();
        productService.addProduct(productRequest, category);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
    }

    @PutMapping("/product/update/{productID}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productID") Long productID, @RequestBody @Valid ProductRequest productRequest) {
        Optional<Category> optionalCategory = categoryService.readCategory(productRequest.getCategoryId());
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
        }
        Category category = optionalCategory.get();
        productService.updateProduct(productID, productRequest, category);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}
