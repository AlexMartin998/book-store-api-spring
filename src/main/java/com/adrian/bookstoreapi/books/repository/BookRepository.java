package com.adrian.bookstoreapi.books.repository;

import com.adrian.bookstoreapi.books.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category")
    Page<Book> fetchAll(Pageable pageable);

}
