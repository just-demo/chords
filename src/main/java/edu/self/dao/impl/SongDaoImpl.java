package edu.self.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.self.dao.PerformerDao;
import edu.self.dao.SongDao;
import edu.self.dao.TagDao;
import edu.self.model.Song;
import edu.self.model.SongSummary;
import edu.self.model.Tag;

@Repository
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
	public void addSong(Song song) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		prepare(song);
		session.save(song);
		session.getTransaction().commit();
	}

	@Override
	public void setSongs(List<Song> songs) {
		// preparePerformers(songs);
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.createQuery("delete from Song").executeUpdate();
		for (Song song : songs) {
			prepare(song);
			session.save(song);
		}
		session.getTransaction().commit();
	}

	private void prepare(Song song) {
		// prepare performer
		song.setPerformer(performerDao.preparePerformer(song.getPerformer()));
		// prepare tags
		song.setTags(tagDao.prepareTags(song.getTags()));
	}

	/*
	 * private void preparePerformers(List<Song> songs) { Map<String, Performer>
	 * preparedPerformers = new HashMap<String, Performer>(); for (Song song :
	 * songs) { Performer performer = song.getPerformer(); if
	 * (preparedPerformers.containsKey(performer.getName())) { performer =
	 * preparedPerformers.get(performer.getName()); } else { performer =
	 * performerDao.preparePerformer(performer);
	 * preparedPerformers.put(performer.getName(), performer); }
	 * song.setPerformer(performer); } }
	 */

	@Override
	public List<Song> getSongs() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Song> songs = session.createQuery("from Song").list();
		// session.getTransaction().commit();
		return songs;
	}
	
	@Override
	public int getSongsCount() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		int count = ((Long)session.createQuery("select count(*) from Song").uniqueResult()).intValue();
		//session.getTransaction().commit();
		return count;
	}

	@Override
	public List<Song> getSongs(int form, int count) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("from Song as song order by song.performer.name");
		query.setFirstResult(form);
		query.setMaxResults(count);
		List<Song> songs = query.list();
		// session.getTransaction().commit();
		return songs;
	}
	
	@Override
	public List<SongSummary> getSongSummaries() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<SongSummary> songs = session.createQuery("from SongSummary").list();
		session.getTransaction().commit();
		return songs;
	}

	@Override
	public Song getSongById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Song song = (Song) session.get(Song.class, id);
		session.getTransaction().commit();
		return song;
	}

	@Override
	public List<SongSummary> getSongSummariesByPerformer(String performer) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<SongSummary> songs = session.createQuery("from SongSummary where performer=:performer").setParameter("performer", performer).list();
		session.getTransaction().commit();
		return songs;
	}

	@Override
	public List<Song> getSongsByPerformer(String performer) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Song> songs = session.createQuery("from Song where performer.name=:performer").setParameter("performer", performer).list();
		session.getTransaction().commit();
		return songs;
	}

	@Override
	public List<SongSummary> getSongSummariesByTag(String tag) {
		List<SongSummary> songs;
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Integer> songIds = session.createSQLQuery("SELECT song_id FROM song_tag WHERE tag_id = (SELECT id FROM tag WHERE tag.name = :tagName)").setParameter("tagName", tag)
				.list();
		if (!songIds.isEmpty()) {
			songs = session.createCriteria(SongSummary.class).add(Restrictions.in("id", songIds)).list();
		} else {
			songs = new ArrayList<SongSummary>();
		}
		session.getTransaction().commit();
		return songs;
	}
	
	@Override
	public List<Song> getSongsByTag(String tagName) {
		List<Song> songs = null;
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Tag tag = (Tag) session.createQuery("from Tag where name=:name").setParameter("name", tagName).uniqueResult();
		if (tag != null) {
			songs = tag.getSongs();
			Hibernate.initialize(songs);
		}
		session.getTransaction().commit();
		return songs;
	}

	public void saveSong(Song song) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		prepare(song);
		// session.update(song);
		session.saveOrUpdate(song);
		session.getTransaction().commit();
	}

	@Override
	public void deleteSong(Song song) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.delete(song);
		session.getTransaction().commit();
	}

	@Override
	public List<SongSummary> searchSongs(String name, String performer) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Criteria criteria = session.createCriteria(SongSummary.class);
		if (name != null) {
			criteria.add(Restrictions.like("name", "%" + name + "%"));
		}
		if (performer != null) {
			criteria.add(Restrictions.like("performer", "%" + performer + "%"));
		}
		List<SongSummary> songs = criteria.list();
		// session.getTransaction().commit();
		return songs;
	}

	@PostConstruct
	public void init() throws InterruptedException {
		Session session = sessionFactory.getCurrentSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		fullTextSession.createIndexer().startAndWait();
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
