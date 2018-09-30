package edu.self.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;

import edu.self.dao.CustomChordDao;
import edu.self.model.ChordCustom;
import edu.self.model.Song;

@Repository
public class CustomChordDaoImpl implements CustomChordDao {
	private static final int TRANSPOSITION_CURCLE = 12;
	
	@Inject
	private SessionFactory sessionFactory;

	@Override
	public void addChord(ChordCustom chord) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(chord);
        session.getTransaction().commit();
	}
	
	@Override
	public void addChords(List<ChordCustom> chords){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        for (ChordCustom chord: chords){
        	session.save(chord);
        }
        session.getTransaction().commit();
	}
	
	@Override
	public ChordCustom getChord(Integer id) {
		return (ChordCustom) sessionFactory.getCurrentSession().get(Song.class, id);
	}

	@Override
	public void saveChord(ChordCustom chord) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ChordCustom> getChords() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
        List<ChordCustom> chords = session.createQuery("from Chord").list();
        session.getTransaction().commit();
        return chords;	
	}

	@Override
	public ChordCustom getChordByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
        ChordCustom chord = (ChordCustom)session.createQuery("from Chord where name=:name").setParameter("name", name).uniqueResult();
        session.getTransaction().commit();
        return chord;
	}

	@Override
	public Set<String> getChordNames() {
		//TODO: try to select a column only
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
        List<ChordCustom> chords = session.createQuery("from Chord").list();
        session.getTransaction().commit();
        Set<String> chordNames = new HashSet<String>();
        for (ChordCustom chord: chords){
        	chordNames.add(chord.getName());
        }
		return chordNames;
	}
	
	@Override
	public Map<String, String> getTranspositionTable(int transposition){
		Map<String, String> transpositionTree = new HashMap<String, String>();
		transposition = reduceTransposition(transposition);
		if (transposition > 0){
			List<ChordCustom> chords = getChords();
			for (ChordCustom chord: chords){
				ChordCustom nextChord = getNext(chord, transposition);
				if (nextChord != null && nextChord != chord){
					transpositionTree.put(chord.getName(), nextChord.getName());
				}
			}
		}
		return transpositionTree;
	}
	
	private ChordCustom getNext(ChordCustom chord, int distance){
		for (int i = 0; i < distance && chord != null; ++i){
			chord = chord.getNext();
		}
		return chord;
	}
	
	private int reduceTransposition(int transposition){
		transposition = (transposition % TRANSPOSITION_CURCLE);
		if (transposition < 0){
			transposition += TRANSPOSITION_CURCLE;
		}
		return transposition;
	}
	
}
