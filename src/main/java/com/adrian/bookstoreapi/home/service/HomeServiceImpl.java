package com.adrian.bookstoreapi.home.service;

import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import com.adrian.bookstoreapi.books.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final BookService bookService;


    @Override
    public PaginatedBooksResponseDto findAllBooks(Pageable pageable) {
        return this.bookService.findAll(pageable);
    }

    @Override
    public BookResponseDto findOneBook(Long id) {
        return bookService.findOne(id);
    }

    @Override
    public List<BookResponseDto> findLatestBooks() {
        return bookService.findLatestBooks();
    }

}
