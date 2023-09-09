package com.adrian.bookstoreapi.books.service;

import com.adrian.bookstoreapi.books.dto.BookRequestDto;
import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.BookUPDRequestDto;
import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BookService {

    PaginatedBooksResponseDto findAll(Pageable pageable);

    BookResponseDto findOne(Long id);

    Set<BookResponseDto> findLatestBooks();

    BookResponseDto create(BookRequestDto bookRequestDto);

    BookResponseDto update(Long id, BookUPDRequestDto bookUPDRequestDto);

}
