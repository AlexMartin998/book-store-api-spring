package com.adrian.bookstoreapi.books.entity;

import com.adrian.bookstoreapi.categories.entity.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;
    private String coverPath;
    private String filePath;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference("category_ref")
    private Category category;


    @PrePersist
    void initCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void initUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }

}
