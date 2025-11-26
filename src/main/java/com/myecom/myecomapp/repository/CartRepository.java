package com.myecom.myecomapp.repository;

import com.myecom.myecomapp.model.Cart;
import com.myecom.myecomapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findByUserUserId(int userId);

    public Cart findByUser(User user);
}
