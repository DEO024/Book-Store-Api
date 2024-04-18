package com.tracy.bookstoreapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            mappedBy = "authors"
    )
    @JsonBackReference
    private Set<Book> books_written = new HashSet<Book>();

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks_written() {
        return books_written;
    }

    public void setBooks_written(Set<Book> books_written) {
        this.books_written = books_written;
    }

    public void addBook(Book book) {
        this.books_written.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook(Book book) {
        this.books_written.remove(book);
        book.getAuthors().remove(this);
    }
}