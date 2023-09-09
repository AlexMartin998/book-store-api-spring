package com.adrian.bookstoreapi.books.service;

import com.adrian.bookstoreapi.books.dto.BookRequestDto;
import com.adrian.bookstoreapi.books.dto.BookResponseDto;
import com.adrian.bookstoreapi.books.dto.BookUPDRequestDto;
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
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public PaginatedBooksResponseDto findAll(Pageable pageable) {
        Page<Book> bookPage = bookRepository.fetchAll(pageable);
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
    public BookResponseDto findOne(Long id) {
        Book book = findOneById(id);

        return modelMapper.map(book, BookResponseDto.class);
    }

    @Override
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        Category category = findOneCategoryById(bookRequestDto.getCategoryId());
        Book savedBook = findOneBySlug(bookRequestDto.getSlug());
        if (savedBook != null)
            throw new BadRequestException("Slug '".concat(bookRequestDto.getSlug()).concat("' already registered"));

        Book book = modelMapper.map(bookRequestDto, Book.class);
        book.setId(null);  // avoid assigning categoryId to bookId and the consequent UPD error
        book.setCategory(category);

        return modelMapper.map(bookRepository.save(book), BookResponseDto.class);
    }

    @Override
    public BookResponseDto update(Long id, BookUPDRequestDto bookUPDRequestDto) {
        Category category = findOneCategoryById(bookUPDRequestDto.getCategoryId());
        findOneById(id);

        Book bookToSave = Book.builder()
                .id(id)
                .category(category)
                .title(bookUPDRequestDto.getTitle())
                .slug(bookUPDRequestDto.getSlug())
                .description(bookUPDRequestDto.getDescription())
                .price(bookUPDRequestDto.getPrice())
                .coverPath(bookUPDRequestDto.getCoverPath())
                .filePath(bookUPDRequestDto.getFilePath())
                .build();


        return modelMapper.map(bookRepository.save(bookToSave), BookResponseDto.class);
    }


    private Category findOneCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
    }

    private Book findOneBySlug(String slug) {
        return bookRepository.findBySlug(slug).orElse(null);
    }

    private Book findOneById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "ID", id));
    }

}
