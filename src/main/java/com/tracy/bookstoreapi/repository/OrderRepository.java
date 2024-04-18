package com.tracy.bookstoreapi.repository;


import com.tracy.bookstoreapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findOrdersByCustomerId(Long customerId);
}

