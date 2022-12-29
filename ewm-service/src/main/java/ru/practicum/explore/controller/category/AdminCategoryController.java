package ru.practicum.explore.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.NewCategoryDto;
import ru.practicum.explore.dto.category.UpdateCategoryDto;
import ru.practicum.explore.service.category.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * API for admin to work with categories
 */

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final CategoryService service;

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Valid UpdateCategoryDto categoryDto,
                                      HttpServletRequest request) {
        log.info("{}: {}; update category {}", request.getRequestURI(), request.getRemoteAddr(),
                categoryDto.toString());
        return service.updateCategory(categoryDto.getId(), categoryDto.getName());
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto categoryDto,
                                      HttpServletRequest request) {
        log.info("{}: {}; create category {}", request.getRequestURI(), request.getRemoteAddr(),
                categoryDto.toString());
        return service.createCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId,
                               HttpServletRequest request) {
        log.info("{}: {}; delete category with id = {}", request.getRequestURI(), request.getRemoteAddr(), catId);
        service.deleteCategory(catId);
    }
}
