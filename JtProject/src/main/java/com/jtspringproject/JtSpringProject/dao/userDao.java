package com.jtspringproject.JtSpringProject.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.sound.midi.Soundbank;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jtspringproject.JtSpringProject.models.User;

@Repository
public class userDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Transactional
	public List<User> getAllUser() {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> userList = session.createQuery("from CUSTOMER").list();
		return userList;
	}

	@Transactional
	public User saveUser(User user) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(user);
		System.out.println("User added" + user.getId());
		return user;
	}

	// public User checkLogin() {
	// this.sessionFactory.getCurrentSession().
	// }
	@Transactional
	public User getUser(String username, String password) {
		Query query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where username = :username");
		query.setParameter("username", username);

		try {
			User user = (User) query.getSingleResult();
			if (password.equals(user.getPassword())) {
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}

	@Transactional
	public boolean usernameExists(String username) {
		Query query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where username = :username");
		query.setParameter("username", username);

		try {
			User user = (User) query.getSingleResult();

			if (user != null) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	@Transactional
	public boolean emailExists(String email) {
		Query query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where email = :email");
		query.setParameter("email", email);

		try {
			User user = (User) query.getSingleResult();

			if (user != null) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	@Transactional
	public void changeUsername(String username, int userID){
		String hql = "update CUSTOMER set username = :newUN where id = :id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("newUN", username);
		query.setParameter("id", userID);

		try {
			int result = query.executeUpdate();
		} catch (Exception e) {

		}
	}

	@Transactional
	public void changeAddress(String address, int userID){
		String hql = "update CUSTOMER set address = :newAddress where id = :id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("newAddress", address);
		query.setParameter("id", userID);

		try {
			int result = query.executeUpdate();
		} catch (Exception e) {

		}
	}
}