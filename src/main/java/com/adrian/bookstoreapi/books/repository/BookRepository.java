package com.adrian.bookstoreapi.books.repository;

import com.adrian.bookstoreapi.books.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b LEFT JOIN FETCH Category c ON c.id = b.category.id")
    Page<Book> fetchAll(Pageable pageable);

    Optional<Book> findBySlug(String slug);

    List<Book> findTop6ByOrderByCreatedAtDesc();

    boolean existsBySlug(String slug);


    // // soft delete
    @Modifying
    @Query("UPDATE Book b SET b.deleted = true, b.active = false WHERE b.id = :id")
    int markAsDeleted(Long id);

}
