package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.controller.UserController;
import com.jtspringproject.JtSpringProject.models.Category;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.categoryService;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.services.cartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private userService userService;

    @Mock
    private productService productService;

    @Mock
    private cartService cartService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Model model;

    @Mock
    private ModelAndView modelAndView;

    private User loggedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        User loggedInUser = new User();
        loggedInUser.setId(2);
        loggedInUser.setUsername("lisa");
        loggedInUser.setAddress("765");

        loggedUser = loggedInUser;

        userController.setLoggedInUser(loggedInUser);
    }

    @Test
    void testSuccessfulLoginNormalUser() {
        // Arrange
        User mockUser = new User();
        mockUser.setRole("ROLE_NORMAL");
        when(userService.checkLogin(anyString(), anyString())).thenReturn(mockUser);
        when(productService.getProducts()).thenReturn(new ArrayList<>());

        // Act
        ModelAndView response = userController.userlogin("lisa", "765", model);

        // Assert
        assertEquals("index", response.getViewName());
        assertNotNull(response.getModel().get("user"));
        assertEquals(mockUser, response.getModel().get("user"));
        assertTrue(response.getModel().containsKey("msg"));
    }

    @Test
    void testSuccessfulLoginAdminUser() {
        // Arrange
        User mockAdminUser = new User();
        mockAdminUser.setRole("ROLE_ADMIN");
        when(userService.checkLogin(anyString(), anyString())).thenReturn(mockAdminUser);

        // Act
        ModelAndView response = userController.userlogin("admin", "123", model);

        // Assert
        assertEquals("adminHome", response.getViewName());
        assertNotNull(response.getModel().get("admin"));
        assertEquals(mockAdminUser, response.getModel().get("admin"));
    }

    @Test
    void testFailedLogin() {
        // Arrange
        when(userService.checkLogin(anyString(), anyString())).thenReturn(null);

        // Act
        ModelAndView response = userController.userlogin("wrongUser", "wrongPass", model);

        // Assert
        assertEquals("userLogin", response.getViewName());
        assertTrue(response.getModel().containsKey("msg"));
        assertEquals("Please enter correct email and password", response.getModel().get("msg"));
    }

    @Test
    void testUpdateProfileSuccess() {
        // Arrange
        String newUsername = "newUsername";
        String newAddress = "newAddress";
        when(userService.usernameExists(newUsername)).thenReturn(false);

        // Act
        ModelAndView mv = userController.updateProfilePOST(newUsername, newAddress);

        // Assert
        assertEquals("redirect:/profileDisplay", mv.getViewName());
    }

    @Test
    void testUpdateProfileFailureUsernameExists() {
        // Arrange
        String existingUsername = "existingUsername";
        String newAddress = "newAddress";
        when(userService.usernameExists(existingUsername)).thenReturn(true);

        // Act
        ModelAndView mv = userController.updateProfilePOST(existingUsername, newAddress);

        // Assert
        verify(userService, never()).changeUsername(anyString(), (int) anyLong());
        assertEquals("redirect:/profileDisplay", mv.getViewName());
        assertTrue(mv.getModel().containsKey("msg"));
        assertEquals("username already taken", mv.getModel().get("msg"));
    }

    @Test
    void testLogout() {
        // Act
        String viewName = userController.returnIndex();

        // Assert
        assertEquals("redirect:/", viewName);
        assertEquals(null, userController.getLoggedInUser());
    }

    @Test
    void testGetProductNoUserLoggedIn() {
        // Simulate no user logged in
        userController.setLoggedInUser(null);

        ModelAndView mv = userController.getProduct();

        assertEquals("userLogin", mv.getViewName());
    }

}
