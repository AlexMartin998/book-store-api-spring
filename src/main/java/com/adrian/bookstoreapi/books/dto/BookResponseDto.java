package com.adrian.bookstoreapi.books.dto;

import com.adrian.bookstoreapi.categories.dto.SimpleCategoryDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class BookResponseDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private String slug;
    private String description;
    private String coverPath;
    private String filePath;
    private boolean active;
    private SimpleCategoryDto category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
