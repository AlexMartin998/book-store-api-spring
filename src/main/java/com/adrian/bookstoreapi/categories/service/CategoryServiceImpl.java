package com.adrian.bookstoreapi.categories.service;

import com.adrian.bookstoreapi.categories.dto.SimpleCategoryAdminResponseDto;
import com.adrian.bookstoreapi.categories.entity.Category;
import com.adrian.bookstoreapi.categories.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<SimpleCategoryAdminResponseDto> findAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> modelMapper.map(category, SimpleCategoryAdminResponseDto.class))
                .toList();
    }

}
