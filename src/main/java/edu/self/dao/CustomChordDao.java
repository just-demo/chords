package edu.self.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.self.model.ChordCustom;

public interface CustomChordDao {
	public void addChord(ChordCustom chord);
	public void addChords(List<ChordCustom> chords);
	public ChordCustom getChord(Integer id);
	public void saveChord(ChordCustom chord);
	public List<ChordCustom> getChords();
	public ChordCustom getChordByName(String name);
	public Set<String> getChordNames();
	public Map<String, String> getTranspositionTable(int transposition);
}
