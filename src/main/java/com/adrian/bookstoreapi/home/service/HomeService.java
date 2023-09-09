package com.adrian.bookstoreapi.home.service;

import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import org.springframework.data.domain.Pageable;

public interface HomeService {

    PaginatedBooksResponseDto findAllBooks(Pageable pageable);

}
