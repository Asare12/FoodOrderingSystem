package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.exception.OrderNotFoundException;
import com.davidasare.FoodOrderingSystem.model.*;
import com.davidasare.FoodOrderingSystem.payload.request.cart.CartItemRequest;
import com.davidasare.FoodOrderingSystem.payload.request.cart.CartRequest;
import com.davidasare.FoodOrderingSystem.payload.request.checkout.CheckoutItemRequest;
import com.davidasare.FoodOrderingSystem.payload.request.order.OrderItemRequest;
import com.davidasare.FoodOrderingSystem.repository.OrderItemsRepository;
import com.davidasare.FoodOrderingSystem.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    public Session createSession(List<CheckoutItemRequest> checkoutItemRequestList) throws StripeException {
        // supply success and failure url for stripe
        String successURL = baseURL + "payment/success";
        String failedURL = baseURL + "payment/failed";

        // set the private key
        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        // for each product compute SessionCreateParams.LineItem
        for (CheckoutItemRequest checkoutItemRequest : checkoutItemRequestList) {
            sessionItemsList.add(createSessionLineItem(checkoutItemRequest));
        }

        // build the session param
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(successURL)
                .build();
        return Session.create(params);
    }

    // build each product in the stripe checkout page
    SessionCreateParams.LineItem createSessionLineItem(CheckoutItemRequest checkoutItemRequest) {
        return SessionCreateParams.LineItem.builder()
                // set price for each product
                .setPriceData(createPriceData(checkoutItemRequest))
                // set quantity for each product
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemRequest.getQuantity())))
                .build();
    }

    // create total price
    SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemRequest checkoutItemRequest) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("gbp")
                .setUnitAmount((long)(checkoutItemRequest.getPrice()*100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(checkoutItemRequest.getProductName())
                                .build())
                .build();
    }

    public void placeOrder(User user, String sessionId) {
        // first let get cart items for the user
        CartRequest cartRequest = cartService.listCartItems(user);

        List<CartItemRequest> cartItemRequestList = cartRequest.getCartItems();

        // create the order and save it
        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setSessionId(sessionId);
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartRequest.getTotalCost());
        orderRepository.save(newOrder);

        for (CartItemRequest cartItemRequest : cartItemRequestList) {
            // create orderItem and save each one
            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(new Date());
            orderItem.setPrice(cartItemRequest.getProduct().getPrice());
            orderItem.setProduct(cartItemRequest.getProduct());
            orderItem.setQuantity(cartItemRequest.getQuantity());
            orderItem.setOrder(newOrder);
            // add to order item list
            orderItemsRepository.save(orderItem);
        }

        cartService.deleteUserCartItems(user);
    }

    public List<Order> listOrders(User user) {
        return orderRepository.findAllByUserOrderByCreatedDateDesc(user);
    }


    public Order getOrder(Long orderId) throws OrderNotFoundException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new OrderNotFoundException("Order not found");
    }

//    public void updateOrder(OrderItemRequest orderItemRequest, User user, Product product) throws OrderNotFoundException {
////        Optional<Order> order = orderRepository.findById(orderId);
//        OrderItem orderItem = orderItemsRepository.getOne(orderItemRequest.getOrderId());
//        orderItem.setQuantity(orderItemRequest.getQuantity());
//        orderItem.setCreatedDate(new Date());
//        orderItemsRepository.save(orderItem);
//    }
//
//    public void deleteOrder(OrderItemRequest orderItemRequest) throws CartItemNotExistException {
//        if (!orderRepository.existsById(orderItemRequest.getOrderId()))
//            throw new CartItemNotExistException("Order id is invalid : " + orderItemRequest.getOrderId());
//        orderRepository.deleteById(orderItemRequest.getOrderId());
//
//    }
}


