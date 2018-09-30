package edu.self.servises.chord;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.self.servises.chord.filter.ClosedStringsUpFilter;
import edu.self.servises.chord.filter.MaxWidthFilter;
import edu.self.servises.chord.filter.NaturalFilter;
import edu.self.types.Chord;
import edu.self.types.Note;

@Service
public class ChordServiceImpl implements ChordService {
	private Generator generator;
	private Filters filters;
	private Sorter sorter;
	
	private Note[] strings = {Note.E,Note.B,Note.G,Note.D,Note.A,Note.E};
	
	public ChordServiceImpl(){
		generator = new Generator();
		generator.setStrings(strings);

		filters = new Filters();
		filters.addFilter(new MaxWidthFilter());
		filters.addFilter(new ClosedStringsUpFilter());
		filters.addFilter(new NaturalFilter());
		
		sorter = new Sorter();
	}
	
	public Set<String> getChordNames(){
		return Chord.getNames();
	}
	
	public List<Integer[]> getChords(String chordName) throws UnsupportedChordException {
		Note[] notes = Chord.getChord(chordName).getNotes();
		generator.setNotes(notes);
		List<Integer[]> chords = generator.getChords();
		chords = filters.apply(chords);
		sorter.sort(chords);
		return chords;
	}

	public Integer[] getChord(String chordName) throws UnsupportedChordException{
		List<Integer[]> chords = getChords(chordName);
		return chords.size() > 0 ? chords.get(0) : null;
	}
	
	public Map<String, String> getTranspositionTable(int transposition){
		Map<String, String> transpositionTable = new HashMap<String, String>();
		Collection<Chord> chords = Chord.getChords();
		for (Chord chord: chords){
			transpositionTable.put(chord.getName(), chord.getNext(transposition).getName());
		}
		return transpositionTable;
	}

	@Override
	public Chord searchChord(Integer[] frets) {
		Set<Note> notes = getNotes(frets);

		for (Chord chord: Chord.getChords()){
			Set<Note> chordNotes = new HashSet<Note>(Arrays.asList(chord.getNotes()));
			if (chordNotes.size() == notes.size() && chordNotes.containsAll(notes)){
				return chord;
			}
		}
		return null;
	}
	
	@Override
	public Set<Note> getNotes(Integer[] frets){
		Set<Note> notes = new HashSet<Note>();
		for (int i = 0; i < strings.length && i < frets.length; ++i){
			if (frets[i] != null){
				notes.add(strings[i].getNext(frets[i]));
			}
		}
		return notes;
	}
}
