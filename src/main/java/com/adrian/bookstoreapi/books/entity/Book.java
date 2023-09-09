package com.adrian.bookstoreapi.books.entity;

import com.adrian.bookstoreapi.categories.entity.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Table("book") // singular form by default
@Where(clause = "deleted = false") // filtra los deleted para todos los Select
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")  // postgres: text type
    private String description;

    @Column(nullable = false)
    private BigDecimal price;
    private String coverPath;
    private String filePath;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference("category_ref")
    private Category category;


    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
