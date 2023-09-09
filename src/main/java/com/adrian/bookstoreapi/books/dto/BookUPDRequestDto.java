package com.adrian.bookstoreapi.books.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class BookUPDRequestDto {

    private String title;
    private BigDecimal price;
    @Pattern(regexp = "[a-z0-9-]+")
    private String slug;
    private String description;
    private String coverPath;
    private String filePath;
    private long categoryId;

}
