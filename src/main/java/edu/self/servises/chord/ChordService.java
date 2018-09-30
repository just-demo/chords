package edu.self.servises.chord;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.self.types.Chord;
import edu.self.types.Note;

public interface ChordService {
	Set<String> getChordNames();
	List<Integer[]> getChords(String chordName) throws UnsupportedChordException;
	Integer[] getChord(String chordName) throws UnsupportedChordException;
	Map<String, String> getTranspositionTable(int transposition);
	Chord searchChord(Integer[] frets);
	Set<Note> getNotes(Integer[] frets);
}
