package com.adrian.bookstoreapi.home.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedBooksHomeResponseDto {

    List<BookHomeResponseDto> books;
    private int pageNumber;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean isLastOne;

}