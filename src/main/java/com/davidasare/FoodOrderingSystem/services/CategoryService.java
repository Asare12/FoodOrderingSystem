package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.model.Category;
import com.davidasare.FoodOrderingSystem.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryrepository;

    public CategoryService(CategoryRepository categoryrepository) {
        this.categoryrepository = categoryrepository;
    }

    public List<Category> listCategories() {
        return categoryrepository.findAll();
    }

    public void createCategory(Category category) {
        categoryrepository.save(category);
    }

    public Category readCategory(String categoryName) {
        return categoryrepository.findByName(categoryName);
    }
    public Optional<Category> readCategory(Long categoryId) {
        return categoryrepository.findById(categoryId);
    }

    public void updateCategory(Long categoryID, Category newCategory) {
        Category category = categoryrepository.findById(categoryID).get();
        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());
        category.setProducts(newCategory.getProducts());
        category.setImageUrl(newCategory.getImageUrl());

        categoryrepository.save(category);
    }

    public void deleteCategory(Long categoryID) {
        categoryrepository.deleteById(categoryID);
    }

}
