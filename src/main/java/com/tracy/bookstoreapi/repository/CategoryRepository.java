package com.tracy.bookstoreapi.repository;


import com.tracy.bookstoreapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT category FROM Category category WHERE " + "LOWER(category.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Category> findByName(@Param("name") String name);
}
