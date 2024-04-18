package com.tracy.bookstoreapi.repository;


import com.tracy.bookstoreapi.model.Role;
import com.tracy.bookstoreapi.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}

