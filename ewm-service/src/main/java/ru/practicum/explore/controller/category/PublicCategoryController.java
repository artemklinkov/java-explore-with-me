package ru.practicum.explore.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.service.category.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Public API to work with categories
 */

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("{}: {}; find categories", request.getRequestURI(), request.getRemoteAddr());
        return categoryService.findCategories(PageRequest.of(from / size, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto findCategoryById(@PathVariable Long catId,
                                        HttpServletRequest request) {
        log.info("{}: {}; find category with id = {}", request.getRequestURI(), request.getRemoteAddr(), catId);
        return categoryService.findCategoryById(catId);
    }
}
