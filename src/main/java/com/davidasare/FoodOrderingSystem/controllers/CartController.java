package com.davidasare.FoodOrderingSystem.controllers;

import com.davidasare.FoodOrderingSystem.exception.AuthenticationFailException;
import com.davidasare.FoodOrderingSystem.exception.CartItemNotExistException;
import com.davidasare.FoodOrderingSystem.exception.ProductNotExistException;
import com.davidasare.FoodOrderingSystem.model.ConfirmationToken;
import com.davidasare.FoodOrderingSystem.model.Product;
import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.model.UserDetailsImpl;
import com.davidasare.FoodOrderingSystem.payload.request.cart.AddToCartRequest;
import com.davidasare.FoodOrderingSystem.payload.request.cart.CartRequest;
import com.davidasare.FoodOrderingSystem.payload.response.ApiResponse;
import com.davidasare.FoodOrderingSystem.services.CartService;
import com.davidasare.FoodOrderingSystem.services.ConfirmationTokenService;
import com.davidasare.FoodOrderingSystem.services.ProductService;
import com.davidasare.FoodOrderingSystem.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @GetMapping("/cart")
    public ResponseEntity<CartRequest> getCartItems(@RequestParam("token") String token) throws AuthenticationFailException {
        CartRequest cartRequest = null;
        if (userDetailsService.isLoggedIn()) {
            User user = confirmationTokenService.getUser(token);
            cartRequest = cartService.listCartItems(user);
        }
        return new ResponseEntity<CartRequest>(cartRequest, HttpStatus.OK);
    }

    @PostMapping("/cart/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartRequest addToCartRequest,
                                                 @RequestParam("token") String token) throws AuthenticationFailException, ProductNotExistException {
        if (userDetailsService.isLoggedIn()) {
            User user = confirmationTokenService.getUser(token);
            Product product = productService.getProductById(addToCartRequest.getProductId());
            cartService.addToCart(addToCartRequest, product, user);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);

    }

    @PutMapping("/cart/update/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(@RequestBody @Valid AddToCartRequest addToCartRequest,
                                                      @RequestParam("token") String token) throws AuthenticationFailException, ProductNotExistException {
        if (userDetailsService.isLoggedIn()) {
            User user = confirmationTokenService.getUser(token);
            Product product = productService.getProductById(addToCartRequest.getProductId());
            cartService.addToCart(addToCartRequest, product, user);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") Long itemID, @RequestParam("token") String token) throws AuthenticationFailException, CartItemNotExistException {

        if (userDetailsService.isLoggedIn()) {
            Long userId = confirmationTokenService.getUser(token).getId();
            cartService.deleteCartItem(itemID, userId);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
    }
}
