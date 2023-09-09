package com.adrian.bookstoreapi.books.service;

import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import org.springframework.data.domain.Pageable;

public interface BookService {

    PaginatedBooksResponseDto findAll(Pageable pageable);

}
