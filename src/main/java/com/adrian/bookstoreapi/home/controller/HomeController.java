package com.adrian.bookstoreapi.home.controller;

import com.adrian.bookstoreapi.common.constants.PaginationConstants;
import com.adrian.bookstoreapi.home.dto.BookHomeResponseDto;
import com.adrian.bookstoreapi.home.dto.PaginatedBooksHomeResponseDto;
import com.adrian.bookstoreapi.home.dto.PaymentOrderRequestDto;
import com.adrian.bookstoreapi.home.service.HomeService;
import com.adrian.bookstoreapi.payments.dto.PayPalOrderResponseDto;
import com.adrian.bookstoreapi.storage.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final StorageService storageService;


    @GetMapping("/books")
    public ResponseEntity<PaginatedBooksHomeResponseDto> getAllBooks(
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_DIR) String sortDir
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(homeService.findAllBooks(pageable));
    }

    @GetMapping("/books/{slug}")
    public ResponseEntity<BookHomeResponseDto> getBook(@PathVariable String slug) {
        return ResponseEntity.ok(homeService.findOneBookBySlug(slug));
    }

    @GetMapping("/books/latest-published")
    public ResponseEntity<List<BookHomeResponseDto>> getLatestBooks() {
        return ResponseEntity.ok(homeService.findLatestBooks());
    }

    // // modify contentType Header Response
    @GetMapping("/files/{filename}")
    ResponseEntity<Resource> getResource(@PathVariable String filename) throws IOException {
        Resource resource = storageService.loadAsResource(filename);
        String contentType = Files.probeContentType(resource.getFile().toPath());  // hacerlo flexible a pdf/png

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }


    // // checkout: free 'cause login is not necessary to buy a book
    // create purchase order
    @PostMapping("/checkout/payment-order")
    ResponseEntity<PayPalOrderResponseDto> createPaymentOrder(@Valid @RequestBody PaymentOrderRequestDto paymentOrderRequestDto) {

        return new ResponseEntity<>(homeService.createPaymentOrder(paymentOrderRequestDto), HttpStatus.CREATED);
    }


}
