package com.luna.warmteaandhonestreviews.service;

import com.luna.warmteaandhonestreviews.domain.CategoryEntity;
import com.luna.warmteaandhonestreviews.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void saveNewCategories(List<String> categories) {
        for (String category : categories) {
            if (!categoryRepository.existsByName(category)) {
                categoryRepository.save(new CategoryEntity(category));
            }
        }
    }
}
