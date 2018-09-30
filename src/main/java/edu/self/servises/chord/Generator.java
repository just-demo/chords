package edu.self.servises.chord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.self.types.Note;

public class Generator {
	private Note[] strings;
	private Note[] notes;

	private Note[] currentNotes;
	private List<Integer[]> chords;
	
	private static final int SEMICIRCLE = Note.CIRCLE / 2;
	
	private void processCurrentChord(){
		//int halfCircle = Note.CIRCLE / 2;
		if (Arrays.asList(currentNotes).containsAll(Arrays.asList(notes))){
			for (int j = 0; j <= 1; ++j){
				int min = Integer.MAX_VALUE;
				boolean expanded = false;
				Integer[] chord  = new Integer[strings.length];
				for (int i = 0; i < strings.length; ++i){
					Integer fret  = null;
					if (currentNotes[i] != null){
						fret = strings[i].getDistance(currentNotes[i]);
						if (j == 1 && fret < SEMICIRCLE){
							fret += Note.CIRCLE;
							expanded = true;
						}
						if (fret < min && fret != 0){
							min = fret;
						}
					}
					chord[i] = fret;
				}
				if (min < Note.CIRCLE && (j != 1 || expanded)){
					chords.add(chord);
				}
			}
		}
	}

	private void fetchChords(int stringIndex){
		boolean isLastString = stringIndex == strings.length - 1;
		for (int i = -1; i < notes.length; ++i){
			currentNotes[stringIndex] = i == -1 ? null : notes[i];
			if (isLastString){
				processCurrentChord();
			}
			else {
				fetchChords(stringIndex + 1);
			}
		}
	}

	public List<Integer[]> getChords(){
		currentNotes = new Note[strings.length];
		chords = new ArrayList<Integer[]>();
		fetchChords(0);
		return chords;
	}

	public Note[] getNotes() {
		return notes;
	}
	public void setNotes(Note ... notes) {
		this.notes = notes;
	}
	public Note[] getStrings() {
		return strings;
	}
	public void setStrings(Note ... strings) {
		this.strings = strings;
	}
}
