package ru.practicum.explore.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.NewCategoryDto;
import ru.practicum.explore.dto.category.mapper.CategoryMapper;
import ru.practicum.explore.handler.exception.ConflictException;
import ru.practicum.explore.handler.exception.NotFoundException;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findCategories(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        return categoryMapper.toDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category is not found")));
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        categoryRepository.findByName(categoryDto.getName()).ifPresent(s -> {
            throw new ConflictException("Category with provided name already exists.");
        });
        Category category = categoryMapper.newToCategory(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, String name) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category is not found"));
        categoryRepository.findByName(name).ifPresent(s -> {
            throw new ConflictException("Category with provided name already exists.");
        });
        category.setName(name);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
