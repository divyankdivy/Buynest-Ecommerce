package com.myecom.myecomapp.controller;

import com.myecom.myecomapp.model.Cart;
import com.myecom.myecomapp.model.CartItem;
import com.myecom.myecomapp.model.User;
import com.myecom.myecomapp.service.CartService;
import com.myecom.myecomapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Comparator;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;  // You must already have this

    @GetMapping
    public String viewCart(Model model, Principal principal) {

        User user = userService.getUserByEmail(principal.getName());
        Cart cart = cartService.getCartByUser(user);

        // sort items by cartItemId so they stay in same order
        if (cart != null && cart.getCartItems() != null) {
            cart.getCartItems().sort(Comparator.comparingInt(CartItem::getCartItemId));
        }

        model.addAttribute("cart", cart);

        return "cart/cart-page";
    }

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable int productId,
                            RedirectAttributes ra,
                            Principal principal) {

        // Safety: if somehow principal is null, redirect to login
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByEmail(principal.getName());

        cartService.addToCart(user, productId);

        // Use same flash message mechanism you use elsewhere
        ra.addFlashAttribute("success", "Product added to cart!");

        // Redirect back to the product page (use the route your app uses)
        return "redirect:/product-view/" + productId + "?added=true";  // adjust if your product URL differs
    }

    @PostMapping("/increase/{productId}")
    public String increaseQuantity(@PathVariable int productId, Principal principal) {

        User user = userService.getUserByEmail(principal.getName());
        cartService.increaseQuantity(user, productId);

        return "redirect:/cart";
    }

    @PostMapping("/decrease/{productId}")
    public String decreaseQuantity(@PathVariable int productId, Principal principal) {

        User user = userService.getUserByEmail(principal.getName());
        cartService.decreaseQuantity(user, productId);

        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeItem(@PathVariable int productId, Principal principal) {

        User user = userService.getUserByEmail(principal.getName());
        cartService.removeFromCart(user, productId);

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model, Principal principal) {

        User user = userService.getUserByEmail(principal.getName());
        Cart cart = cartService.getCartByUser(user);

        if (cart == null || cart.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("user", user);

        return "cart/checkout";
    }
}
