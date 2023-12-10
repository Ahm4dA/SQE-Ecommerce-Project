package com.jtspringproject.JtSpringProject.Service;

import com.jtspringproject.JtSpringProject.dao.cartDao;
import com.jtspringproject.JtSpringProject.models.Cart;
import com.jtspringproject.JtSpringProject.services.cartService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private cartDao cartDao;

    @InjectMocks
    private cartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCart() {
        Cart cart = new Cart();

        when(cartDao.addCart(cart)).thenReturn(cart);

        Cart result = cartService.addCart(cart);

        assertEquals(cart, result);
        verify(cartDao, times(1)).addCart(cart);
    }

    @Test
    void testGetCarts() {
        List<Cart> carts = new ArrayList<>();
        carts.add(new Cart());

        when(cartDao.getCarts()).thenReturn(carts);

        List<Cart> result = cartService.getCarts();

        assertEquals(carts, result);
        verify(cartDao, times(1)).getCarts();
    }

    @Test
    void testUpdateCart() {
        Cart cart = new Cart();

        cartService.updateCart(cart);

        verify(cartDao, times(1)).updateCart(cart);
    }

    @Test
    void testDeleteCart() {
        Cart cart = new Cart();

        cartService.deleteCart(cart);

        verify(cartDao, times(1)).deleteCart(cart);
    }

}

