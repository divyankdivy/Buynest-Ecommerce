package com.myecom.myecomapp.repository;

import com.myecom.myecomapp.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    CartItem findByCartCartIdAndProductProductId(int cartId, int productId);

}
