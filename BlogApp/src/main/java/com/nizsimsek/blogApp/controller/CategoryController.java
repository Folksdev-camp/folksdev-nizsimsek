package com.nizsimsek.blogApp.controller;


import com.nizsimsek.blogApp.dto.CategoryDto;
import com.nizsimsek.blogApp.dto.request.CreateCategoryReq;
import com.nizsimsek.blogApp.dto.request.UpdateCategoryReq;
import com.nizsimsek.blogApp.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryReq createCategoryReq) {
        return ResponseEntity.ok(categoryService.createCategory(createCategoryReq));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String id, @Valid @RequestBody UpdateCategoryReq updateCategoryReq) {
        return ResponseEntity.ok(categoryService.updateCategory(id, updateCategoryReq));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable String id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Category deleted..");
    }
}
