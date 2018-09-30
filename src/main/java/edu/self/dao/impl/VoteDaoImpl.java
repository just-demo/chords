package edu.self.dao.impl;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import edu.self.dao.VoteDao;
import edu.self.model.Song;
import edu.self.model.User;
import edu.self.model.Vote;

@Service
public class VoteDaoImpl implements VoteDao {
	@Inject
	private SessionFactory sessionFactory;

	@Override
	public Boolean voted(Song song, User user) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Vote vote = (Vote) session.createQuery("from Vote where userId=:userId and songId=:songId").setParameter("userId", user.getId()).setParameter("songId", song.getId()).uniqueResult();
		session.getTransaction().commit();
		return vote != null;
	}

	@Override
	public void setVote(Song song, User user, Integer voteValue) {
		if (voted(song, user)){
			return;
		}
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Vote vote = new Vote();
		vote.setUserId(user.getId());
		vote.setSongId(song.getId());
		vote.setVote(voteValue);
		session.save(vote);
		session.getTransaction().commit();
	}

}
