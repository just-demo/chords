package edu.self.dao.impl;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;

import edu.self.dao.UserDao;
import edu.self.model.ChordCustom;
import edu.self.model.Song;
import edu.self.model.User;

@Repository
public class UserDaoImpl implements UserDao {
	@Inject
	private SessionFactory sessionFactory;

	@Override
	public User getUserById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		User user = (User) session.get(Song.class, id);
		session.getTransaction().commit();
		return user;
	}
	
	@Override
	public User getUserByName(String username) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
        User user = (User)session.createQuery("from User where username=:username").setParameter("username", username).uniqueResult();
        session.getTransaction().commit();
        return user;
	}

	@Override
	public void saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
	}
}
