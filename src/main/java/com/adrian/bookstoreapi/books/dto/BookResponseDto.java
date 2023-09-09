package com.adrian.bookstoreapi.books.dto;

import com.adrian.bookstoreapi.categories.dto.SimpleCategoryDto;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class BookResponseDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private String slug;
    private String description;
    private String coverPath;
    private String filePath;
    private Long categoryId;
    private SimpleCategoryDto category;

}
