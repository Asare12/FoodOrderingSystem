package com.davidasare.FoodOrderingSystem.repository;

import com.davidasare.FoodOrderingSystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

//    private final List<Category> ALL_CATEGORIES = new ArrayList<>(Arrays.asList(
//            new Category(1, "Chicken"),
//            new Category(2, "Beef"),
//            new Category(1, "Fish"),
//            new Category(1, "Rice")
//    ));
}
