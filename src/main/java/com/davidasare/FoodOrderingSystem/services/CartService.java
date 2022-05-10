package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.exception.CartItemNotExistException;
import com.davidasare.FoodOrderingSystem.model.Cart;
import com.davidasare.FoodOrderingSystem.model.Product;
import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.payload.request.cart.AddToCartRequest;
import com.davidasare.FoodOrderingSystem.payload.request.cart.CartItemRequest;
import com.davidasare.FoodOrderingSystem.payload.request.cart.CartRequest;
import com.davidasare.FoodOrderingSystem.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CartService {

    @Autowired
    private final CartRepository cartRepository;

    public void addToCart(AddToCartRequest addToCartRequest, Product product, User user){
        Cart cart = new Cart(user, product, addToCartRequest.getQuantity());
        cartRepository.save(cart);
    }

    public CartRequest listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemRequest> cartItems = new ArrayList<>();
        for (Cart cart : cartList){
            CartItemRequest cartItemRequest = getRequestFromCart(cart);
            cartItems.add(cartItemRequest);
        }
        double totalCost = 0;
        for (CartItemRequest cartItemRequest : cartItems){
            totalCost += (cartItemRequest.getProduct().getPrice() * cartItemRequest.getQuantity());
        }
        return new CartRequest(cartItems,totalCost);
    }

    public static CartItemRequest getRequestFromCart(Cart cart) {
        return new CartItemRequest(cart);
    }

    public void updateCartItem(AddToCartRequest cartRequest, User user,Product product){
        Cart cart = cartRepository.getOne(cartRequest.getId());
        cart.setQuantity(cartRequest.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
    }

    public void deleteCartItem(Long id,Long userId) throws CartItemNotExistException {
        if (!cartRepository.existsById(id))
            throw new CartItemNotExistException("Cart id is invalid : " + id);
        cartRepository.deleteById(id);

    }
    public void deleteCartItems(int userId) {
        cartRepository.deleteAll();
    }


    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }

}
