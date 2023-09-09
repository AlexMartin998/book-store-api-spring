package com.adrian.bookstoreapi.categories.entity;

import com.adrian.bookstoreapi.books.entity.Book;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.List;


@Data
@Entity
@Where(clause = "deleted = false")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonManagedReference("category_ref")
    private List<Book> books;

}
