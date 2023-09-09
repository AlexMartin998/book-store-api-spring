package com.adrian.bookstoreapi.books.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class BookRequestDto {

    @NotBlank  // white spaces not allowed
    @Size(min = 3, max = 120)
    private String title;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotBlank
    @Pattern(regexp = "[a-z0-9-]+")
    private String slug;

    @NotBlank
    private String description;

    @NotBlank
    private String coverPath;

    @NotBlank
    private String filePath;

    @NotNull
    private Long categoryId;

}
