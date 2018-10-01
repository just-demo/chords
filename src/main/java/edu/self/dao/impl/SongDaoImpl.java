package edu.self.dao.impl;

import edu.self.dao.PerformerDao;
import edu.self.dao.SongDao;
import edu.self.dao.TagDao;
import edu.self.model.Song;
import edu.self.model.Tag;
import org.hibernate.*;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Repository
@Transactional
public class SongDaoImpl implements SongDao {
    private SessionFactory sessionFactory;

    @Inject
    private PerformerDao performerDao;

    @Inject
    private TagDao tagDao;

    @Autowired
    public SongDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void setSongs(List<Song> songs) {
        // preparePerformers(songs);
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        session.createQuery("delete from Song").executeUpdate();
        for (Song song : songs) {
            prepare(song);
            session.save(song);
        }
        //session.getTransaction().commit();
    }

    private void prepare(Song song) {
        // prepare performer
        song.setPerformer(performerDao.preparePerformer(song.getPerformer()));
        // prepare tags
        song.setTags(tagDao.prepareTags(song.getTags()));
    }

    @Override
    public List<Song> getSongs() {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        List<Song> songs = session.createQuery("from Song").list();
        // //session.getTransaction().commit();
        return songs;
    }

    @Override
    public int getSongsCount() {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        int count = ((Long) session.createQuery("select count(*) from Song").uniqueResult()).intValue();
        ////session.getTransaction().commit();
        return count;
    }

    @Override
    public List<Song> getSongs(int form, int count) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        Query query = session.createQuery("from Song as song order by song.performer.name");
        query.setFirstResult(form);
        query.setMaxResults(count);
        List<Song> songs = query.list();
        // //session.getTransaction().commit();
        return songs;
    }

    @Override
    public Song getSongById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        Song song = (Song) session.get(Song.class, id);
        //session.getTransaction().commit();
        return song;
    }

    @Override
    public List<Song> getSongsByPerformer(String performer) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        List<Song> songs = session.createQuery("from Song where performer.name=:performer").setParameter("performer", performer).list();
        //session.getTransaction().commit();
        return songs;
    }

    @Override
    public List<Song> getSongsByTag(String tagName) {
        List<Song> songs = null;
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        Tag tag = (Tag) session.createQuery("from Tag where name=:name").setParameter("name", tagName).uniqueResult();
        if (tag != null) {
            songs = tag.getSongs();
            Hibernate.initialize(songs);
        }
        //session.getTransaction().commit();
        return songs;
    }

    public void saveSong(Song song) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        prepare(song);
        // session.update(song);
        session.saveOrUpdate(song);
        //session.getTransaction().commit();
    }

    @Override
    public void deleteSong(Song song) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        session.delete(song);
        //session.getTransaction().commit();
    }

    @PostConstruct
    public void init() throws InterruptedException {
        // TODO: fix full text search
//        Session session = sessionFactory.getCurrentSession();
//        FullTextSession fullTextSession = Search.getFullTextSession(session);
//        fullTextSession.createIndexer().startAndWait();
    }

    @Override
    public List<Song> searchSongs(String text) {
        Session session = sessionFactory.getCurrentSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        Transaction tx = fullTextSession.beginTransaction();
        QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Song.class).get();
        org.apache.lucene.search.Query query = qb.keyword().onFields("name", "text", "performer.name").matching(text).createQuery();
        // wrap Lucene query in a org.hibernate.Query
        org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Song.class);
        // execute search
        List<Song> result = hibQuery.list();
        tx.commit();
        // session.close();
        return result;
    }
}
