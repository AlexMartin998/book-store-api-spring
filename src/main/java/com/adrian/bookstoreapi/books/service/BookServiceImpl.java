package com.adrian.bookstoreapi.books.service;

import com.adrian.bookstoreapi.books.dto.BookRequestDto;
import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import com.adrian.bookstoreapi.books.entity.Book;
import com.adrian.bookstoreapi.books.repository.BookRepository;
import com.adrian.bookstoreapi.categories.entity.Category;
import com.adrian.bookstoreapi.categories.repository.CategoryRepository;
import com.adrian.bookstoreapi.common.exceptions.BadRequestException;
import com.adrian.bookstoreapi.common.exceptions.ResourceNotFoundException;
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
    private final CategoryRepository categoryRepository;
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

    @Override
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        Category category = findOneCategoryById(bookRequestDto.getCategoryId());
        Book savedBook = findOneBySlug(bookRequestDto.getSlug());
        if (savedBook != null)
            throw new BadRequestException("Slug '".concat(bookRequestDto.getSlug()).concat("' already registered"));

        Book book = Book.builder()
                .category(category)
                .title(bookRequestDto.getTitle())
                .slug(bookRequestDto.getSlug())
                .price(bookRequestDto.getPrice())
                .description(bookRequestDto.getDescription())
                .deleted(false)
                .build();

        return modelMapper.map(bookRepository.save(book), BookResponseDto.class);
    }


    private Category findOneCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
    }

    private Book findOneBySlug(String slug) {
        return bookRepository.findBySlug(slug).orElse(null);
    }

}
