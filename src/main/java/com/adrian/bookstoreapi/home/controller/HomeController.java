package com.adrian.bookstoreapi.home.controller;

import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import com.adrian.bookstoreapi.books.service.BookService;
import com.adrian.bookstoreapi.common.constants.PaginationConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final BookService bookService;

    @GetMapping("courses")
    public ResponseEntity<PaginatedBooksResponseDto> getAll(
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

}
