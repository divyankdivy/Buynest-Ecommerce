package com.myecom.myecomapp.service;

import com.myecom.myecomapp.model.Cart;
import com.myecom.myecomapp.model.User;

public interface CartService {

    public Cart getCartByUser(User user);

    public Cart createCart(User user);

    public void addToCart(User user, int productId);

    public void updateCartTotal(Cart cart);

    public void increaseQuantity(User user, int productId);

    public void decreaseQuantity(User user, int productId);

    public void removeFromCart(User user, int cartItemId);
}
