package com.myecom.myecomapp.repository;

import com.myecom.myecomapp.model.Order;
import com.myecom.myecomapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    public List<Order> findByUserUserId(int userId);

    public List<Order> findByUserOrderByOrderDateDesc(User user);

    public Boolean existsByOrderNumber(String orderNumber);

    Order findByOrderNumber(String orderNumber);

}
