package com.adrian.bookstoreapi.books.service;

import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import com.adrian.bookstoreapi.books.entity.Book;
import com.adrian.bookstoreapi.books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;


    @Override
    public PaginatedBooksResponseDto findAll(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        List<BookResponseDto> bookDtoList = bookPage.getContent().stream()
                .map(book -> modelMapper.map(book, BookResponseDto.class))
                .toList();

        return PaginatedBooksResponseDto.builder()
                .books(bookDtoList)
                .pageNumber(bookPage.getNumber())
                .size(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .isLastOne(bookPage.isLast())
                .build();
    }

}
