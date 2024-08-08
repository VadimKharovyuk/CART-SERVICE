package com.example.cartservice.service;

import com.example.cartservice.dto.CartDto;
import com.example.cartservice.dto.CartItemDto;
import com.example.cartservice.model.Cart;
import com.example.cartservice.model.CartItem;
import com.example.cartservice.repository.CartItemRepository;
import com.example.cartservice.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CartService {

        private static final Logger logger = LoggerFactory.getLogger(CartService.class);

        @Autowired
        private CartRepository cartRepository;

        @Autowired
        private CartItemRepository cartItemRepository;

        public CartDto getCart(Long userId) {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        logger.info("Cart not found for user id {}", userId);
                        return new Cart(); // Consider if a new cart should be created or handle this case differently
                    });
            return convertToDto(cart); // You need to create this method
        }

        public void addItemToCart(CartItemDto cartItemDto) {
            // Ensure the cart exists or create it
            Cart cart = cartRepository.findByUserId(cartItemDto.getUserId())
                    .orElseGet(() -> {
                        Cart newCart = new Cart();
                        newCart.setUserId(cartItemDto.getUserId());
                        return cartRepository.save(newCart);
                    });

            // Create CartItem from CartItemDto
            CartItem cartItem = new CartItem();
            cartItem.setProductId(cartItemDto.getProductId());
            cartItem.setProductName(cartItemDto.getProductName());
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setCart(cart);

            // Save CartItem to database
            cartItemRepository.save(cartItem);

            // Update cart with new item
            cart.getItems().add(cartItem);
            cartRepository.save(cart);
        }

        // Convert Cart to CartDto
        private CartDto convertToDto(Cart cart) {
            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setUserId(cart.getUserId());
            cartDto.setItems(cart.getItems().stream()
                    .map(item -> {
                        CartItemDto itemDto = new CartItemDto();
                        itemDto.setId(item.getId());
                        itemDto.setProductId(item.getProductId());
                        itemDto.setProductName(item.getProductName());
                        itemDto.setQuantity(item.getQuantity());
                        return itemDto;
                    })
                    .collect(Collectors.toSet()));
            return cartDto;
        }
    }


