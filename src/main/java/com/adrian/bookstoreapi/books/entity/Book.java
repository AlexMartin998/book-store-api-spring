package com.adrian.bookstoreapi.books.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
//@Table("book") // singular form by default
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String slug;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;
    private String coverPath;
    private String filePath;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @PrePersist
    void initCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void initUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }

}
