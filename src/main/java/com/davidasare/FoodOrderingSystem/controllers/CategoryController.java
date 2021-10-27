package com.davidasare.FoodOrderingSystem.controllers;

import com.davidasare.FoodOrderingSystem.model.Category;
import com.davidasare.FoodOrderingSystem.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id){
        return categoryRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public ResponseEntity createCategory(@RequestBody Category category) throws URISyntaxException {
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.created(new URI("/category/" + savedCategory.getId())).body(savedCategory);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody Category category) {
         Category currentCategory = categoryRepository.findById(id).orElseThrow(RuntimeException::new);
         currentCategory.setName(category.getName());
         categoryRepository.save(currentCategory);
         return ResponseEntity.ok(currentCategory);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id){
        categoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
