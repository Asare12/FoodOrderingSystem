package com.davidasare.FoodOrderingSystem.controllers;

import com.davidasare.FoodOrderingSystem.exception.AuthenticationFailException;
import com.davidasare.FoodOrderingSystem.model.Order;
import com.davidasare.FoodOrderingSystem.model.User;
import com.davidasare.FoodOrderingSystem.payload.request.checkout.CheckoutItemRequest;
import com.davidasare.FoodOrderingSystem.payload.request.checkout.StripeResponse;
import com.davidasare.FoodOrderingSystem.payload.request.order.OrderItemRequest;
import com.davidasare.FoodOrderingSystem.payload.response.ApiResponse;
import com.davidasare.FoodOrderingSystem.services.ConfirmationTokenService;
import com.davidasare.FoodOrderingSystem.services.OrderService;
import com.davidasare.FoodOrderingSystem.services.ProductService;
import com.davidasare.FoodOrderingSystem.services.UserDetailsServiceImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ProductService productService;

    // get all orders
    @GetMapping("/order")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token) throws AuthenticationFailException {
        List<Order> orderList = null;
        // validate token
        if (userDetailsService.isLoggedIn()) {
            // retrieve user
            User user = confirmationTokenService.getUser(token);
            // get orders
            orderList = orderService.listOrders(user);
        }
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    // get orderItems for an order
    @GetMapping("/order/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") Long id)
            throws AuthenticationFailException {
        Order order = null;
        // validate token
        if (userDetailsService.isLoggedIn()) {
            order = orderService.getOrder(id);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // stripe create session API
    @PostMapping("/order/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemRequest> checkoutItemRequestList)
            throws StripeException {
        // create the stripe session
        Session session = orderService.createSession(checkoutItemRequestList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        // send the stripe session id in response
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    // place order after checkout
    @PostMapping("/order/add")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
            throws AuthenticationFailException {
        // validate token
        if (userDetailsService.isLoggedIn()) {
            // retrieve user
            User user = confirmationTokenService.getUser(token);
            // place the order
            orderService.placeOrder(user, sessionId);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
    }
//    @PutMapping("/order/update/{orderId}")
//    public ResponseEntity<ApiResponse> updateOrderItem(@RequestBody @Valid OrderItemRequest orderItemRequest,
//                                                      @RequestParam("token") String token) throws AuthenticationFailException, ProductNotExistException {
//        if (userDetailsService.isLoggedIn()) {
//            User user = confirmationTokenService.getUser(token);
//            Product product = productService.getProductById(orderItemRequest.getProductId());
//            orderService.updateOrder(orderItemRequest, user,product);
//        }
//        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Order has been updated"), HttpStatus.OK);
//
//    }
//    @DeleteMapping("/order/delete/{orderId}")
//    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("orderId") OrderItemRequest orderItemRequest , @RequestParam("token") String token) throws AuthenticationFailException, CartItemNotExistException {
//
//        if (userDetailsService.isLoggedIn()) {
//            Long userId = confirmationTokenService.getUser(token).getId();
//            orderService.deleteOrder(orderItemRequest);
//        }
//        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
//    }
}
