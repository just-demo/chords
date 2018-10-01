package edu.self.dao.impl;

import edu.self.dao.CustomChordDao;
import edu.self.model.ChordCustom;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Repository
@Transactional
public class CustomChordDaoImpl implements CustomChordDao {

    @Inject
    private SessionFactory sessionFactory;

    @Override
    public void addChord(ChordCustom chord) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        session.save(chord);
        //session.getTransaction().commit();
    }

    @Override
    public void addChords(List<ChordCustom> chords) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        for (ChordCustom chord : chords) {
            session.save(chord);
        }
        //session.getTransaction().commit();
    }

    @Override
    public List<ChordCustom> getChords() {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        List<ChordCustom> chords = session.createQuery("from Chord").list();
        //session.getTransaction().commit();
        return chords;
    }

    @Override
    public ChordCustom getChordByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        //session.beginTransaction();
        ChordCustom chord = (ChordCustom) session.createQuery("from Chord where name=:name").setParameter("name", name).uniqueResult();
        //session.getTransaction().commit();
        return chord;
    }

}
