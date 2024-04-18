package com.tracy.bookstoreapi.controller;


import com.tracy.bookstoreapi.model.Order;
import com.tracy.bookstoreapi.payload.CreateOrderRequest;
import com.tracy.bookstoreapi.security.CurrentUser;
import com.tracy.bookstoreapi.security.UserPrincipal;
import com.tracy.bookstoreapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> addOrder(@CurrentUser UserPrincipal currentUser, @RequestBody CreateOrderRequest order) {
        System.out.println(currentUser);
        Order savedOrder = orderService.save(currentUser, order);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedOrder.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedOrder);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable long id) {
        Optional<Order> order = orderService.findOrderById(id);

        return order.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable long id, @RequestBody Order order) {
        Optional<Order> existingOrder = orderService.findOrderById(id);
        Order updatedOrder;

        if (existingOrder.isPresent()) {
            updatedOrder = orderService.updateOrder(order);
            return ResponseEntity.ok().body(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable long id) {
        Optional<Order> order = orderService.findOrderById(id);

        if (order.isPresent()) {
            orderService.deleteOrderById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

