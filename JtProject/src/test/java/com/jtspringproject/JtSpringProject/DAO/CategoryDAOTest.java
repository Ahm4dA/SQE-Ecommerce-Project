package com.jtspringproject.JtSpringProject.DAO;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.jtspringproject.JtSpringProject.dao.categoryDao;
import com.jtspringproject.JtSpringProject.models.Category;

@SpringBootTest
@Transactional
@Rollback(true)
class CategoryDAOTest {

    @Autowired
    private categoryDao categoryDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testAddCategory() {
        Category category = categoryDao.addCategory("TestCategory");
        assertNotNull(category);
        assertEquals("TestCategory", category.getName());
    }

    @Test
    void testDeleteCategory() {
        Category testCategory = categoryDao.addCategory("TestCategory");
        assertTrue(categoryDao.deletCategory(testCategory.getId()));
        assertNull(sessionFactory.getCurrentSession().get(Category.class, testCategory.getId()));
    }

    @Test
    void testUpdateCategory() {
        Category testCategory = categoryDao.addCategory("TestCategory");
        Category updatedCategory = categoryDao.updateCategory(testCategory.getId(), "UpdatedCategory");
        assertEquals("UpdatedCategory", updatedCategory.getName());
    }

    @Test
    void testGetCategory() {
        Category testCategory = categoryDao.addCategory("TestCategory");
        Category retrievedCategory = categoryDao.getCategory(testCategory.getId());
        assertNotNull(retrievedCategory);
        assertEquals("TestCategory", retrievedCategory.getName());
    }    
}
