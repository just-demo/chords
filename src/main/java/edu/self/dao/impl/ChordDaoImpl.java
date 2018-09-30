package edu.self.dao.impl;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import edu.self.dao.ChordDao;
import edu.self.dao.UserDao;
import edu.self.model.Chord;
import edu.self.model.User;

@Service
public class ChordDaoImpl implements ChordDao {
	@Inject
	private SessionFactory sessionFactory;

	@Inject
	private UserDao userDao;

	@Override
	public String getFrets(String userName, String chordName) {
		User user = userDao.getUserByName(userName);
		Chord chord = getChord(user, chordName);
		if (chord != null) {
			return chord.getFrets();
		}
		return null;
	}

	private Chord getChord(User user, String chordName) {
		if (user != null) {
			for (Chord chord : user.getChords()) {
				if (chord.getName().equals(chordName)) {
					return chord;
				}
			}
		}
		return null;
	}

	@Override
	public void setFrets(String userName, String chordName, String frets) {
		User user = userDao.getUserByName(userName);
		Chord chord = getChord(user, chordName);
		if (chord == null) {
			chord = new Chord();
			chord.setName(chordName);
			chord.setUser(user);
			// TODO: is it necessary???: user.getChords().add(chord);
		}
		chord.setFrets(frets);

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.saveOrUpdate(chord);
		session.getTransaction().commit();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
