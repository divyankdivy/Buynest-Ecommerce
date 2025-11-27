package com.myecom.myecomapp.service;

import com.myecom.myecomapp.model.Order;
import com.myecom.myecomapp.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    Order placeOrder(User user, String paymentMethod);

    public List<Order> getOrdersByUser(User user);

    public Page<Order> getAllOrdersPaginated(int page, int size);

    Order getOrderByOrderNumber(String orderNumber);

}
