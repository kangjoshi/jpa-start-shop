package edu.til.jpastartshop.service;

import edu.til.jpastartshop.domain.Category;
import edu.til.jpastartshop.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceTest {
    /*
     * 카테고리 등록
     * 카테고리 조회
     * */
    @MockBean
    CategoryRepository categoryRepository;

    CategoryService categoryService;

    @BeforeAll
    public void init() {
        this.categoryService = new CategoryService(categoryRepository);
    }

    @Test
    public void givenCategoryThenReturnCreatedCategory() {
        Category category = new Category("도서");

        when(categoryRepository.save(category)).thenReturn(category);

        Category created = categoryService.create(category);

        assertNotNull(created);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void givenCategoryIdThenReturnCategory() {
        long categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category("도서")));

        Category category = categoryService.findById(categoryId);

        assertNotNull(category);
        verify(categoryRepository, times(1)).findById(categoryId);
    }


}