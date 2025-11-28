package com.myecom.myecomapp.serviceimpl;

import com.myecom.myecomapp.model.Cart;
import com.myecom.myecomapp.model.CartItem;
import com.myecom.myecomapp.model.Product;
import com.myecom.myecomapp.model.User;
import com.myecom.myecomapp.repository.CartItemRepository;
import com.myecom.myecomapp.repository.CartRepository;
import com.myecom.myecomapp.repository.ProductRepository;
import com.myecom.myecomapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private ProductRepository productRepo;

    @Override
    public Cart getCartByUser(User user) {
        return cartRepo.findByUserUserId(user.getUserId());
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(0);
        return cartRepo.save(cart);
    }

    @Override
    public void addToCart(User user, int productId) {
        Product product = productRepo.findById(productId).orElse(null);

        Cart cart = getCartByUser(user);

        if (cart == null) {
            cart = createCart(user);
        }

        CartItem existingItem = cartItemRepo
                .findByCartCartIdAndProductProductId(cart.getCartId(), productId);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            existingItem.setPrice(existingItem.getQuantity() * product.getPrice());
            cartItemRepo.save(existingItem);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(1);
            item.setPrice(product.getPrice());
            cartItemRepo.save(item);
        }

        updateCartTotal(cart);
    }

    @Override
    public void updateCartTotal(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getPrice)
                .sum();

        cart.setTotalPrice(total);
        cartRepo.save(cart);
    }

    @Override
    public void increaseQuantity(User user, int productId) {
        Cart cart = getCartByUser(user);

        if (cart == null) return;

        CartItem item = cartItemRepo.findByCartCartIdAndProductProductId(cart.getCartId(), productId);

        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            cartItemRepo.save(item);

            updateCartTotal(cart);
        }
    }

    @Override
    public void decreaseQuantity(User user, int productId) {

        Cart cart = getCartByUser(user);

        if (cart == null) return;

        CartItem item = cartItemRepo.findByCartCartIdAndProductProductId(cart.getCartId(), productId);

        if (item != null) {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                item.setPrice(item.getQuantity() * item.getProduct().getPrice());
                cartItemRepo.save(item);
            }

            else {
                cartItemRepo.delete(item);
            }
            updateCartTotal(cart);
        }
    }

    @Override
    public void removeFromCart(User user, int cartItemId) {

        Cart cart = getCartByUser(user);

        if (cart == null) return;

        CartItem item = cartItemRepo.findById(cartItemId).orElse(null);

        if (item != null && item.getCart().getCartId() == cart.getCartId()) {
            cartItemRepo.delete(item);
            updateCartTotal(cart);
        }
    }
}
