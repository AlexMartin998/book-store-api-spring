package com.adrian.bookstoreapi.books.controller;

import com.adrian.bookstoreapi.books.dto.BookRequestDto;
import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.BookUPDRequestDto;
import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import com.adrian.bookstoreapi.books.service.BookService;
import com.adrian.bookstoreapi.common.constants.PaginationConstants;
import com.adrian.bookstoreapi.common.constants.RoleConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<PaginatedBooksResponseDto> getAllBooks(
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_DIR) String sortDir
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<BookResponseDto> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findOne(id));
    }

    @PatchMapping("/{id}")
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<BookResponseDto> update(@PathVariable Long id, @Valid @RequestBody BookUPDRequestDto bookUPDRequestDto) {
        return ResponseEntity.ok(bookService.update(id, bookUPDRequestDto));
    }

}
