package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.models.Cart;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.jtspringproject.JtSpringProject.services.cartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.cartService;

@Controller
public class UserController {

	@Autowired
	private userService userService;

	@Autowired
	private productService productService;

	@GetMapping("/register")
	public String registerUser() {
		return "register";
	}

	@GetMapping("/buy")
	public String buy() {
		return "buy";
	}

	@GetMapping("/")
	public String userlogin(Model model) {

		return "userLogin";
	}

	@RequestMapping(value = "userloginvalidate", method = RequestMethod.POST)
	public ModelAndView userlogin(@RequestParam("username") String username, @RequestParam("password") String pass,
			Model model, HttpServletResponse res) {

		User u = this.userService.checkLogin(username, pass);

		if (u != null) {
			// System.out.println(u.getRole());
			if (u.getRole().contains("ROLE_NORMAL")) {
				res.addCookie(new Cookie("username", u.getUsername()));
				ModelAndView mView = new ModelAndView("index");
				mView.addObject("user", u);
				List<Product> products = this.productService.getProducts();

				if (products.isEmpty()) {
					mView.addObject("msg", "No products are available");
				} else {
					mView.addObject("products", products);
				}
				return mView;
			} else {
				ModelAndView mv = new ModelAndView("adminHome");
				// adminlogcheck=1;
				mv.addObject("admin", u);
				return mv;
			}

		} else {
			ModelAndView mView = new ModelAndView("userLogin");
			mView.addObject("msg", "Please enter correct email and password");
			return mView;
		}

	}

	@GetMapping("/user/products")
	public ModelAndView getproduct() {

		ModelAndView mView = new ModelAndView("uproduct");

		List<Product> products = this.productService.getProducts();

		if (products.isEmpty()) {
			mView.addObject("msg", "No products are available");
		} else {
			mView.addObject("products", products);
		}

		return mView;
	}

	@RequestMapping(value = "newuserregister", method = RequestMethod.POST)
	public ModelAndView newUseRegister(@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("address") String address) {

		if (username.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty()) {
			ModelAndView mView = new ModelAndView("register");
			mView.addObject("msg", "Please enter all the details");
			return mView;
		}

		if (this.userService.usernameExists(username) || this.userService.emailExists(email)) {
			ModelAndView mView = new ModelAndView("register");
			mView.addObject("msg", "Please enter new username and email");
			return mView;
		} else {
			User uu = new User();
			uu.setUsername(username);
			uu.setAddress(address);
			uu.setEmail(email);
			uu.setRole("ROLE_NORMAL");
			uu.setPassword(password);

			this.userService.addUser(uu);

			ModelAndView mView = new ModelAndView("redirect:/");
			return mView;
		}
	}

	// for Learning purpose of model
	@GetMapping("/test")
	public String Test(Model model) {
		System.out.println("test page");
		model.addAttribute("author", "jay gajera");
		model.addAttribute("id", 40);

		List<String> friends = new ArrayList<String>();
		model.addAttribute("f", friends);
		friends.add("xyz");
		friends.add("abc");

		return "test";
	}

	// for learning purpose of model and view ( how data is pass to view)

	@GetMapping("/test2")
	public ModelAndView Test2() {
		System.out.println("test page");
		// create modelandview object
		ModelAndView mv = new ModelAndView();
		mv.addObject("name", "jay gajera 17");
		mv.addObject("id", 40);
		mv.setViewName("test2");

		List<Integer> list = new ArrayList<Integer>();
		list.add(10);
		list.add(25);
		mv.addObject("marks", list);
		return mv;

	}

	@GetMapping("profileDisplay")
	public String profileDisplay(Model model) {

		return "updateProfile";
	}

	@RequestMapping(value = { "/", "/logout" })
	public String returnIndex() {
		return "redirect:/";
	}

	// @GetMapping("carts")
	// public ModelAndView getCartDetail()
	// {
	// ModelAndView mv= new ModelAndView();
	// List<Cart>carts = cartService.getCarts();
	// }

}