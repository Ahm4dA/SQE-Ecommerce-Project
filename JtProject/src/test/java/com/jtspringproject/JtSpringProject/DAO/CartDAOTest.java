package com.jtspringproject.JtSpringProject.DAO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.jtspringproject.JtSpringProject.dao.cartDao;
import com.jtspringproject.JtSpringProject.models.Cart;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {cartDao.class})
public class CartDAOTest {

    @Autowired
    private cartDao cartDao;

    @MockBean
    private SessionFactory sessionFactory;

    @MockBean
    private Session session;

    @BeforeEach
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testAddCart() {
        Cart cart = new Cart();
        when(session.save(cart)).thenReturn(1);

        Cart result = cartDao.addCart(cart);

        assertNotNull(result);
        assertEquals(cart, result);
        verify(session).save(cart);
    }


    @Test
    public void testUpdateCart() {
        Cart cart = new Cart();
        cartDao.updateCart(cart);

        verify(session).update(cart);
    }

    @Test
    public void testDeleteCart() {
        Cart cart = new Cart();
        cartDao.deleteCart(cart);

        verify(session).delete(cart);
    }
}
