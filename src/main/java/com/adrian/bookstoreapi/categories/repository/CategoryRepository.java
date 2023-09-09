package com.adrian.bookstoreapi.categories.repository;

import com.adrian.bookstoreapi.categories.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository  extends JpaRepository<Category, Long> {

}
