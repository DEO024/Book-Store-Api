package com.tracy.bookstoreapi.controller;

import com.tracy.bookstoreapi.model.Author;
import com.tracy.bookstoreapi.payload.AuthorRequest;
import com.tracy.bookstoreapi.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Author> addAuthor(@Valid @RequestBody AuthorRequest author) {
        Author authorEntity = new Author(author.getName());
        Author savedAuthor = authorService.save(authorEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAuthor.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedAuthor);
    }

    @GetMapping
    public ResponseEntity<?> getAuthors(@RequestParam(value = "name", required = false) String name) {
        List<Author> authors;
        System.out.println(name);
        if (name != null) {
            authors = authorService.findAuthorByName(name);
        } else {
            authors = authorService.findAllAuthors();
        }
        return ResponseEntity.ok().body(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable long id) {
        Optional<Author> author = authorService.findAuthorById(id);

        return author.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author) {
        Optional<Author> existingAuthor = authorService.findAuthorById(author.getId());
        Author updatedAuthor;

        if (existingAuthor.isPresent()) {
            updatedAuthor = authorService.updateAuthor(author);
            return ResponseEntity.ok().body(updatedAuthor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        Optional<Author> existingAuthor = authorService.findAuthorById(id);

        if (existingAuthor.isPresent()) {
            authorService.deleteAuthorById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

//TODO: add methods for adding and removing books from an author
}