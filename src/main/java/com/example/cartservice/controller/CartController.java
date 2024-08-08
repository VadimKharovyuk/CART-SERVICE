package com.example.cartservice.controller;

import com.example.cartservice.dto.CartDto;
import com.example.cartservice.dto.CartItemDto;
import com.example.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Получить корзину по userId
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable("userId") Long userId) {
        try {
            CartDto cartDto = cartService.getCart(userId);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        } catch (Exception e) {
            // Логирование и возврат ошибки
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Если корзина не найдена
        }
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItemToCart(@RequestBody CartItemDto cartItemDto) {
        try {
            cartService.addItemToCart(cartItemDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            // Логирование и возврат ошибки
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Если корзина не найдена или ошибка в запросе
        }
    }
}
