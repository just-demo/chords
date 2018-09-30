package edu.self.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.self.servises.chord.UnsupportedChordException;

public class Chord {
	private static Map<String, Chord> chordByName = new HashMap<String, Chord>();
	static {
		for (Type type: Type.values()){
			for (Note note: Note.values()){
				Chord chord = new Chord(note, type);
				chordByName.put(chord.getName(), chord);
			}
		}
	}
	
	private Note note;
	private Type type;
	private String name;
	
	private Chord(Note note, Type type){
		this.note = note;
		this.type = type;
		this.name = composeName(note, type);
	}
	
	/*
	@Override
	public boolean equals(Object chord){
		if (chord == null){
			return false;
		}
		if (!(chord instanceof Chord)){
			return false;
		}
		return name.equals(((Chord) chord).name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	*/
	
	public Chord getNext(int transposition){
		return getChord(note.getNext(transposition), type);
	}
	
	public Chord getNext(){
		return getNext(1);
	}
	
	public Note getNote() {
		return note;
	}

	public Type getType() {
		return type;
	}

	public String getName(){
		return name;
	}
	
	public static Set<String> getNames(){
		return chordByName.keySet();
	}
	
	public static Chord getChord(String name) throws UnsupportedChordException{
		if (!chordByName.containsKey(name)){
			throw new UnsupportedChordException("Unsupported chord name");
		}
		return chordByName.get(name);
	}
	
	public static Chord getChord(Note note, Type type){
		return chordByName.get(composeName(note, type));
	}
	
	public static Collection<Chord> getChords(){
		return chordByName.values();
	}
	
	public Note[] getNotes() {
		int [] distances = type.getDistances();
		Note[] notes = new Note[distances.length + 1];
		notes[0] = note;
		for (int i = 0; i < distances.length; ++i){
			notes[i + 1] = notes[i].getNext(distances[i]);
		}
		return notes;
	}
	
	private static String composeName(Note note, Type type){
		return note.getName() + type.getName();
	}
}