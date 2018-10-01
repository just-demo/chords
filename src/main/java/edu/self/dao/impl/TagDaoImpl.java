package edu.self.dao.impl;

import edu.self.dao.TagDao;
import edu.self.model.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

@Repository
@Transactional
public class TagDaoImpl implements TagDao {
    @Inject
    private SessionFactory sessionFactory;

    @Override
    public Tag getTagByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        Tag tag = (Tag) session.createQuery("from Tag where name=:name").setParameter("name", name).uniqueResult();
        ////session.getTransaction().commit();
        return tag;
    }

    @Override
    public Tag prepareTag(String name) {
        Tag tag = getTagByName(name);
        if (tag == null) {
            tag = new Tag();
            tag.setName(name);
        }
        return tag;
    }


    @Override
    public List<Tag> getTags() {
        Session session = sessionFactory.getCurrentSession();
//        //session.beginTransaction();
        List<Tag> tags = session.createQuery("from Tag").list();
//        //session.getTransaction().commit();
        return tags;
    }

    @Override
    public Set<Tag> prepareTags(Collection<Tag> tags) {
        //TODO: change this method
        Map<String, Tag> preparedTags = new HashMap<String, Tag>();
        Set<Tag> tagSet = new HashSet<Tag>();
        for (Tag tag : tags) {
            if (!preparedTags.containsKey(tag.getName())) {
                tag = prepareTag(tag.getName());
                preparedTags.put(tag.getName(), tag);
            }
            tagSet.add(preparedTags.get(tag.getName()));
        }
        //List<Tag> tags = session.createCriteria(Tag.class).add(Restrictions.in("name", tagNames)).list();
        return tagSet;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
