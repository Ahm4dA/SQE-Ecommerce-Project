package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.dao.categoryDao;
import com.jtspringproject.JtSpringProject.models.Category;
import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.productService;
import com.jtspringproject.JtSpringProject.services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

	private User loggedInUser = null;

	@Autowired
	private userService userService;

	@Autowired
	private productService productService;
	@Autowired
	private categoryDao categoryService;

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

	public User getLoggedInUser(){
		return this.loggedInUser;
	}

	public void setLoggedInUser(User u){
		this.loggedInUser = u;
	}


	@RequestMapping(value = "userloginvalidate", method = RequestMethod.POST)
	public ModelAndView userlogin(@RequestParam("username") String username, @RequestParam("password") String pass,
								  Model model) {

		User u = this.userService.checkLogin(username, pass);

		if (u != null) {
			if (u.getRole().contains("ROLE_NORMAL")) {
				this.loggedInUser = u;
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
				this.loggedInUser = u;
				ModelAndView mv = new ModelAndView("adminHome");
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

	@GetMapping("profileDisplay")
	public ModelAndView profileDisplay(Model model) {

		ModelAndView mv = new ModelAndView("profileView");
		mv.addObject("user", loggedInUser);

		return mv;
	}

	@GetMapping("updateProfile")
	public ModelAndView updateProfile(Model model) {

		ModelAndView mv = new ModelAndView("updateProfile");
		mv.addObject("user", loggedInUser);

		return mv;
	}

	@PostMapping("updateProfile")
	public ModelAndView updateProfilePOST(@RequestParam("username") String username, @RequestParam("address") String address) {

		ModelAndView mv = new ModelAndView("redirect:/profileDisplay");

		if(!username.isEmpty()){
			if(!this.loggedInUser.getUsername().equals(username)){
				if(this.userService.usernameExists(username)){
					mv.addObject("msg", "username already taken");
				}
				else{
					this.userService.changeUsername(username, this.loggedInUser.getId());
					this.loggedInUser.setUsername(username);
				}
			}
		}

		if(!address.isEmpty()) {
			if (!this.loggedInUser.getAddress().equals(address)) {
				this.userService.changeAddress(address, this.loggedInUser.getId());
				this.loggedInUser.setAddress(address);
			}
		}

		mv.addObject("user", loggedInUser);
		return mv;
	}

	@RequestMapping(value = { "/", "/logout" })
	public String returnIndex() {
		this.loggedInUser = null;
		return "redirect:/";
	}

	//--------------Admin Part--------------------

	@GetMapping("admin/products")
	public ModelAndView getProduct() {

		ModelAndView mView;

		if(loggedInUser != null){
			if (!loggedInUser.getRole().equals("ROLE_ADMIN")) {
				mView = new ModelAndView("adminlogin");
			}
			else{
				mView = new ModelAndView("products");

				List<Product> products = this.productService.getProducts();

				if (products.isEmpty()) {
					mView.addObject("msg", "No products are available");
				} else {
					mView.addObject("products", products);
				}
			}
		}
		else {
			mView = new ModelAndView("userLogin");
		}
		return mView;

	}
	@GetMapping("admin/products/add")
	public ModelAndView addProduct() {
		ModelAndView mView = new ModelAndView("productsAdd");
		List<Category> categories = this.categoryService.getCategories();
		mView.addObject("categories",categories);
		return mView;
	}

	@RequestMapping(value = "admin/products/add",method=RequestMethod.POST)
	public String addProduct(@RequestParam("name") String name,@RequestParam("categoryid") int categoryId ,@RequestParam("price") int price,@RequestParam("weight") int weight, @RequestParam("quantity")int quantity,@RequestParam("description") String description,@RequestParam("productImage") String productImage) {

		if(this.productService.productByNameExists(name)){
			return "redirect:/admin/products";
		}

		System.out.println(categoryId);
		Category category = this.categoryService.getCategory(categoryId);
		Product product = new Product();
		product.setId(categoryId);
		product.setName(name);
		product.setCategory(category);
		product.setDescription(description);
		product.setPrice(price);
		product.setImage(productImage);
		product.setWeight(weight);
		product.setQuantity(quantity);
		this.productService.addProduct(product);
		return "redirect:/admin/products";
	}

	@GetMapping("admin/products/update/{id}")
	public ModelAndView updateproduct(@PathVariable("id") int id) {

		ModelAndView mView = new ModelAndView("productsUpdate");
		Product product = this.productService.getProduct(id);
		System.out.println(product.getId());
		List<Category> categories = this.categoryService.getCategories();

		mView.addObject("categories",categories);
		mView.addObject("product", product);
		return mView;
	}

	@RequestMapping(value = "products/update/{id}",method=RequestMethod.POST)
	public String updateProduct(@PathVariable("id") int id ,@RequestParam("name") String name,@RequestParam("categoryid") int categoryId ,@RequestParam("price") int price,@RequestParam("weight") int weight, @RequestParam("quantity")int quantity,@RequestParam("description") String description,@RequestParam("productImage") String productImage)
	{
		if(description.isEmpty() || productImage.isEmpty()){
			return "redirect:/admin/products";
		}

		Category category = this.categoryService.getCategory(categoryId);
		Product product = new Product();
		product.setId(categoryId);
		product.setName(name);
		product.setCategory(category);
		product.setDescription(description);
		product.setPrice(price);
		product.setImage(productImage);
		product.setWeight(weight);
		product.setQuantity(quantity);

		this.productService.updateProduct(id, product);
		return "redirect:/admin/products";
	}

	@PostMapping("admin/products/delete")
	public String removeProduct(@RequestParam("id") int id)
	{
		this.productService.deleteProduct(id);
		return "redirect:/admin/products";
	}

	// @GetMapping("carts")
	// public ModelAndView getCartDetail()
	// {
	// ModelAndView mv= new ModelAndView();
	// List<Cart>carts = cartService.getCarts();
	// }

}