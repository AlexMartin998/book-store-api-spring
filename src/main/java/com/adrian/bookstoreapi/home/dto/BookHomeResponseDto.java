package com.adrian.bookstoreapi.home.dto;

import com.adrian.bookstoreapi.categories.dto.SimpleCategoryDto;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class BookHomeResponseDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private String slug;
    private String description;
    private String coverPath;
    private String filePath;
    private SimpleCategoryDto category;

}
