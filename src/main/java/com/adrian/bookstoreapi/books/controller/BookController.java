package com.adrian.bookstoreapi.books.controller;

import com.adrian.bookstoreapi.books.dto.BookRequestDto;
import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.BookUPDRequestDto;
import com.adrian.bookstoreapi.books.service.BookService;
import com.adrian.bookstoreapi.common.constants.RoleConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @PostMapping
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto bookRequestDto) {
        return new ResponseEntity<>(bookService.create(bookRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<BookResponseDto> update(@PathVariable Long id, @Valid @RequestBody BookUPDRequestDto bookUPDRequestDto) {
        return ResponseEntity.ok(bookService.update(id, bookUPDRequestDto));
    }

}
