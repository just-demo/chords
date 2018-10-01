package edu.self.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.self.model.ChordCustom;

public interface CustomChordDao {
	void addChord(ChordCustom chord);
	void addChords(List<ChordCustom> chords);
	List<ChordCustom> getChords();
	ChordCustom getChordByName(String name);
}
