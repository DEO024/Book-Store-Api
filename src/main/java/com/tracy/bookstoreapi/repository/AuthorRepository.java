package com.tracy.bookstoreapi.repository;

import com.tracy.bookstoreapi.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT author FROM Author author WHERE " + "LOWER(author.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Author> findByName(@Param("name") String name);
}
