package com.adrian.bookstoreapi.categories.service;


import com.adrian.bookstoreapi.categories.dto.SimpleCategoryAdminResponseDto;

import java.util.List;

public interface CategoryService {

    List<SimpleCategoryAdminResponseDto> findAll();

}
