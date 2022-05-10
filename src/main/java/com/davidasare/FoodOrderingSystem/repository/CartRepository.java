package com.davidasare.FoodOrderingSystem.repository;

import com.davidasare.FoodOrderingSystem.model.Cart;
import com.davidasare.FoodOrderingSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    List<Cart> deleteByUser(User user);
}
