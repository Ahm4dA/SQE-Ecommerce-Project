package com.jtspringproject.JtSpringProject.Service;

import com.jtspringproject.JtSpringProject.dao.categoryDao;
import com.jtspringproject.JtSpringProject.models.Category;
import com.jtspringproject.JtSpringProject.services.categoryService;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private categoryDao categoryDao;

    @InjectMocks
    private categoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategory() {
        String categoryName = "TestCategory";
        Category category = new Category();
        category.setName(categoryName);

        when(categoryDao.addCategory(categoryName)).thenReturn(category);

        Category result = categoryService.addCategory(categoryName);

        assertEquals(category, result);
        verify(categoryDao, times(1)).addCategory(categoryName);
    }

    @Test
    void testGetCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());

        when(categoryDao.getCategories()).thenReturn(categories);

        List<Category> result = categoryService.getCategories();

        assertEquals(categories, result);
        verify(categoryDao, times(1)).getCategories();
    }

    @Test
    void testDeleteCategory() {
        int categoryId = 1;

        when(categoryDao.deletCategory(categoryId)).thenReturn(true);

        boolean result = categoryService.deleteCategory(categoryId);

        assertTrue(result);
        verify(categoryDao, times(1)).deletCategory(categoryId);
    }

    @Test
    void testUpdateCategory() {
        int categoryId = 1;
        String newName = "UpdatedCategoryName";
        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(newName);

        when(categoryDao.updateCategory(categoryId, newName)).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory(categoryId, newName);

        assertEquals(updatedCategory, result);
        verify(categoryDao, times(1)).updateCategory(categoryId, newName);
    }

    @Test
    void testGetCategory() {
        int categoryId = 1;
        Category category = new Category();

        when(categoryDao.getCategory(categoryId)).thenReturn(category);

        Category result = categoryService.getCategory(categoryId);

        assertEquals(category, result);
        verify(categoryDao, times(1)).getCategory(categoryId);
    }
}
