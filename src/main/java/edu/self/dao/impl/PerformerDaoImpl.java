package edu.self.dao.impl;

import edu.self.dao.PerformerDao;
import edu.self.model.Performer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Repository
@Transactional
public class PerformerDaoImpl implements PerformerDao {
    @Inject
    private SessionFactory sessionFactory;

    @Override
    public Performer getPerformerByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        Performer performer = (Performer) session.createQuery("from Performer where name=:name").setParameter("name", name).uniqueResult();
        ////session.getTransaction().commit(); //it must not be commited because the method is executed as an inner transaction. TODO: examine!!!
        return performer;
    }

    @Override
    public List<Performer> getPerformers() {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        List<Performer> performers = session.createQuery("from Performer").list();
        //session.getTransaction().commit();
        return performers;
    }

    @Override
    public Performer preparePerformer(String name) {
        Performer performer = getPerformerByName(name);
        if (performer == null) {
            performer = new Performer();
            performer.setName(name);
        }
        return performer;
    }

    @Override
    public Performer preparePerformer(Performer performer) {
        return preparePerformer(performer.getName());
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
