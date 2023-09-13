package com.adrian.bookstoreapi.home.service;

import com.adrian.bookstoreapi.home.dto.BookHomeResponseDto;
import com.adrian.bookstoreapi.home.dto.PaginatedBooksHomeResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HomeService {

    PaginatedBooksHomeResponseDto findAllBooks(Pageable pageable);

    BookHomeResponseDto findOneBookBySlug(String slug);

    List<BookHomeResponseDto> findLatestBooks();

}
