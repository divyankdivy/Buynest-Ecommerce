package com.myecom.myecomapp.serviceimpl;

import com.myecom.myecomapp.model.*;
import com.myecom.myecomapp.repository.CartItemRepository;
import com.myecom.myecomapp.repository.CartRepository;
import com.myecom.myecomapp.repository.OrderRepository;
import com.myecom.myecomapp.repository.ProductRepository;
import com.myecom.myecomapp.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private ProductRepository productRepo;

    @Override
    @Transactional
    public Order placeOrder(User user, String paymentMethod) {

        Cart cart = cartRepo.findByUserUserId(user.getUserId());

        if (cart == null || cart.getCartItems().isEmpty()) {
            return null;
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(paymentMethod);
        order.setStatus("Placed");

        String orderNumber;
        do {
            orderNumber = generateOrderNumber();
        } while (orderRepo.existsByOrderNumber(orderNumber));
        order.setOrderNumber(orderNumber);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cart.getCartItems()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            double price = cartItem.getProduct().getPrice() * cartItem.getQuantity();
            orderItem.setPrice(price);

            total += price;
            orderItems.add(orderItem);

            // ⭐ STOCK REDUCTION HERE
            Product product = cartItem.getProduct();
            int currentStock = product.getStock();

            if (currentStock >= cartItem.getQuantity()) {
                product.setStock(currentStock - cartItem.getQuantity());
            } else {
                product.setStock(0);
            }

            productRepo.save(product); // update DB
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepo.save(order);

        cartItemRepo.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepo.save(cart);

        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepo.findByUserOrderByOrderDateDesc(user);
    }

    private String generateOrderNumber() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }

        return "ODR_" + builder.toString();  // Example: ODR_X4A9KQ
    }

    @Override
    public Page<Order> getAllOrdersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepo.findAll(pageable);
    }

    @Override
    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepo.findByOrderNumber(orderNumber);
    }
}
