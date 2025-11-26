package com.myecom.myecomapp.repository;

import com.myecom.myecomapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
