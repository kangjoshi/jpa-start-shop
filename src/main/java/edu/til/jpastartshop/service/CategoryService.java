package edu.til.jpastartshop.service;

import edu.til.jpastartshop.domain.Category;
import edu.til.jpastartshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category findById(long categoryId) {
        if (categoryId == 0) {
            throw new IllegalArgumentException("잘못된 형식의 카테고리 아이디입니다.");
        }

        return categoryRepository.findById(categoryId).orElseThrow(NoSuchElementException::new);
    }
}
