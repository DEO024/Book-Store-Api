package com.tracy.bookstoreapi.controller;

import com.tracy.bookstoreapi.model.Book;
import com.tracy.bookstoreapi.payload.CreateBookRequest;
import com.tracy.bookstoreapi.payload.PagedResponse;
import com.tracy.bookstoreapi.service.BookService;
import com.tracy.bookstoreapi.util.AppConstants;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> addBook(@Valid @RequestBody CreateBookRequest book) {
        Book savedBook = bookService.save(book);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBook.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedBook);
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks(@RequestParam(value = "title", required = false) String title) {
        List<Book> books;

        if (title != null) {
            books = bookService.findBookByTitle(title);
        } else {
            books = bookService.findAllBooks();
        }
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/all")
    public PagedResponse<Book> getBooks(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return bookService.allBooks(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Optional<Book> book = bookService.findBookById(id);

        return book.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Optional<Book> existingBook = bookService.findBookById(book.getId());
        Book updatedBook;

        if (existingBook.isPresent()) {
            updatedBook = bookService.updateBook(book);
            return ResponseEntity.ok().body(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Optional<Book> existingBook = bookService.findBookById(id);

        if (existingBook.isPresent()) {
            bookService.deleteBookById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    //TODO: add more methods for other functionality
}