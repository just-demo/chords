package edu.self.dao.impl;

import edu.self.dao.UserDao;
import edu.self.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {
    @Inject
    private SessionFactory sessionFactory;

    @Override
    public User getUserByName(String username) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        User user = (User) session.createQuery("from User where username=:username").setParameter("username", username).uniqueResult();
        //session.getTransaction().commit();
        return user;
    }

    @Override
    public void saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        session.save(user);
        //session.getTransaction().commit();
    }
}
