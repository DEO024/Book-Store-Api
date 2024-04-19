package com.tracy.bookstoreapi.service;


import com.tracy.bookstoreapi.model.Book;
import com.tracy.bookstoreapi.model.Order;
import com.tracy.bookstoreapi.model.User;
import com.tracy.bookstoreapi.payload.CreateOrderRequest;
import com.tracy.bookstoreapi.repository.BookRepository;
import com.tracy.bookstoreapi.repository.OrderRepository;
import com.tracy.bookstoreapi.repository.UserRepository;
import com.tracy.bookstoreapi.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    public Order save(UserPrincipal currentUser, CreateOrderRequest order) {
        Order newOrder = new Order();

        List<Book> booksList = bookRepository.findByIdIn(order.getBookIds());
        Optional<User> user = userRepository.findById(currentUser.getId());

        user.ifPresent(newOrder::setCustomer);
        newOrder.getBooks().addAll(booksList);

        Instant now = Instant.now();
        newOrder.setCreatedAt(now);
        newOrder.setUpdatedAt(now);

        newOrder.setCost(
                booksList.stream()
                        .mapToLong(Book::getPrice)
                        .sum()
        );

        return orderRepository.save(newOrder);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<List<Order>> findOrdersByUserId(Long userId) {
        return orderRepository.findOrdersByCustomerId(userId);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}

