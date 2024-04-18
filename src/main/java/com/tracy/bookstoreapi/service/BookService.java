package com.tracy.bookstoreapi.service;

import com.tracy.bookstoreapi.model.Author;
import com.tracy.bookstoreapi.model.Book;
import com.tracy.bookstoreapi.model.Category;
import com.tracy.bookstoreapi.payload.CreateBookRequest;
import com.tracy.bookstoreapi.payload.PagedResponse;
import com.tracy.bookstoreapi.repository.AuthorRepository;
import com.tracy.bookstoreapi.repository.BookRepository;
import com.tracy.bookstoreapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Book save(CreateBookRequest book) {
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setDescription(book.getDescription());

        book.getAuthors().forEach(authorId -> {
//            find the author with the attached id
            Optional<Author> author = authorRepository.findById(authorId);

//            add the author to the list
            author.ifPresent(value -> newBook.getAuthors().add(value));
        });

        book.getCategories().forEach(categoryId -> {
            Optional<Category> category = categoryRepository.findById(categoryId);

            category.ifPresent(value -> newBook.getCategories().add(value));
        });

        return bookRepository.save(newBook);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public PagedResponse<Book> allBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC);
        Page<Book> allBooks = bookRepository.findAll(pageable);

        if (allBooks.getNumberOfElements() == 0) {
            return new PagedResponse<>(
                    Collections.emptyList(),
                    allBooks.getNumber(),
                    allBooks.getSize(),
                    allBooks.getTotalElements(),
                    allBooks.getTotalPages(),
                    allBooks.isLast());
        }

        return new PagedResponse<>(
                allBooks.getContent(),
                allBooks.getNumber(),
                allBooks.getSize(),
                allBooks.getTotalElements(),
                allBooks.getTotalPages(),
                allBooks.isLast());

    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
