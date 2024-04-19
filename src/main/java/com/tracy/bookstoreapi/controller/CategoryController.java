package com.tracy.bookstoreapi.controller;

import com.tracy.bookstoreapi.model.Category;
import com.tracy.bookstoreapi.payload.CreateCategoryRequest;
import com.tracy.bookstoreapi.service.CategoryService;
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
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody CreateCategoryRequest category) {
        Category categoryEntity = new Category(category.getName());
        Category savedCategory = categoryService.save(categoryEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedCategory);
    }

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(value = "name", required = false) String name) {
        List<Category> categories;

        if (name != null) {
            categories = categoryService.findCategoryByName(name);
        } else {
            categories = categoryService.findAllCategories();
        }
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.findCategoryById(id);

        return category.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        Optional<Category> existingCategory = categoryService.findCategoryById(category.getId());
        Category updatedCategory;

        if (existingCategory.isPresent()) {
            updatedCategory = categoryService.updateCategory(category);
            return ResponseEntity.ok().body(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        Optional<Category> category = categoryService.findCategoryById(id);

        if (category.isPresent()) {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    //What's left to do: add methods for adding and removing books from a category
}