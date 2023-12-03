package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.models.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtspringproject.JtSpringProject.dao.userDao;
import com.jtspringproject.JtSpringProject.models.User;

@Service
public class userService {
	@Autowired
	private userDao userDao;

	public List<User> getUsers() {
		return this.userDao.getAllUser();
	}

	public User addUser(User user) {
		return this.userDao.saveUser(user);
	}

	public User checkLogin(String username, String password) {
		return this.userDao.getUser(username, password);
	}

	public boolean usernameExists(String username) {
		return this.userDao.usernameExists(username);
	}

	public boolean emailExists(String email) {
		return this.userDao.emailExists(email);
	}

	public void changeUsername(String username, int id){
		this.userDao.changeUsername(username, id);
	}

	public void changeAddress(String username, int id){
		this.userDao.changeAddress(username, id);
	}
}