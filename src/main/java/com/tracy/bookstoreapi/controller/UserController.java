package com.tracy.bookstoreapi.controller;


import com.tracy.bookstoreapi.model.User;
import com.tracy.bookstoreapi.payload.UserSummary;
import com.tracy.bookstoreapi.security.CurrentUser;
import com.tracy.bookstoreapi.security.UserPrincipal;
import com.tracy.bookstoreapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserSummary> getAllUsers() {
        List<User> users = userService.findAllUsers();
        List<UserSummary> userSummaryList;

        userSummaryList = users.stream()
                .map(user -> new UserSummary(user.getId(), user.getName(), user.getEmail()))
                .toList();

        return userSummaryList;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.id(), currentUser.name(), currentUser.email());
    }
}
