package com.adrian.bookstoreapi.home.service;

import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import com.adrian.bookstoreapi.books.service.BookService;
import com.adrian.bookstoreapi.home.controller.PaginatedBooksHomeResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final BookService bookService;
    private final ModelMapper modelMapper;


    @Override
    public PaginatedBooksHomeResponseDto findAllBooks(Pageable pageable) {
        PaginatedBooksResponseDto paginatedBooksResponseDto = this.bookService.findAll(pageable);

        return modelMapper.map(paginatedBooksResponseDto, PaginatedBooksHomeResponseDto.class);
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
