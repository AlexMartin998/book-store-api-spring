package com.adrian.bookstoreapi.categories.controller;

import com.adrian.bookstoreapi.categories.entity.Category;
import com.adrian.bookstoreapi.categories.repository.CategoryRepository;
import com.adrian.bookstoreapi.categories.service.CategoryService;
import com.adrian.bookstoreapi.common.constants.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
//@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/api/v1/home/categories")
//    @GetMapping
//    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

}
