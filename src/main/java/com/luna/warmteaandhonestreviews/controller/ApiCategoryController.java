package com.luna.warmteaandhonestreviews.controller;

import com.luna.warmteaandhonestreviews.dto.CategoryDto;
import com.luna.warmteaandhonestreviews.dto.GetCategoriesRespDto;
import com.luna.warmteaandhonestreviews.service.CategoryService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class ApiCategoryController {

    private final CategoryService categoryService;


    public ApiCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetCategoriesRespDto getCategories() {
        List<CategoryDto> categoryDtos = categoryService.findAll();
        return new GetCategoriesRespDto(categoryDtos);
    }
}
