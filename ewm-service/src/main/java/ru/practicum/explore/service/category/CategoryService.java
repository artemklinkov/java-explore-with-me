package ru.practicum.explore.service.category;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> findCategories(PageRequest pageRequest);

    CategoryDto findCategoryById(Long catId);

    CategoryDto createCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(Long catId, String name);

    void deleteCategory(Long categoryId);
}
