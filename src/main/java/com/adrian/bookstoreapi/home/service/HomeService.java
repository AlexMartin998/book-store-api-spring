package com.adrian.bookstoreapi.home.service;

import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HomeService {

    PaginatedBooksResponseDto findAllBooks(Pageable pageable);

    BookResponseDto findOneBook(Long id);

    List<BookResponseDto> findLatestBooks();

}
