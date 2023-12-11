package com.jtspringproject.JtSpringProject.DAO;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.jtspringproject.JtSpringProject.dao.productDao;
import com.jtspringproject.JtSpringProject.models.Category;
import com.jtspringproject.JtSpringProject.models.Product;

@SpringBootTest
@Transactional
@Rollback(true)
class ProductDAOTest {

    @Autowired
    private productDao productDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGetProducts() {
        List<Product> initialProducts = productDao.getProducts();
        Product testProduct = new Product();
        productDao.addProduct(testProduct);
        List<Product> productsAfterAddition = productDao.getProducts();

        assertEquals(initialProducts.size() + 1, productsAfterAddition.size());
        assertTrue(productsAfterAddition.contains(testProduct));
    }

    @Test
    void testAddProduct() {
        Product product = new Product();
        productDao.addProduct(product);
        assertNotNull(product.getId());
    }

    @Test
    void testGetProduct() {
        Product testProduct = new Product();

        productDao.addProduct(testProduct);

        Product retrievedProduct = productDao.getProduct(testProduct.getId());
        assertNotNull(retrievedProduct);
    }

    @Test
    void testUpdateProduct() {
        Product testProduct = new Product();
        productDao.addProduct(testProduct);
        Product updatedProduct = productDao.updateProduct(testProduct);
    }

    @Test
    void testDeleteProduct() {
        Product testProduct = new Product();
        productDao.addProduct(testProduct);

        assertTrue(productDao.deletProduct(testProduct.getId()));
        assertNull(sessionFactory.getCurrentSession().get(Product.class, testProduct.getId()));
    }
}
