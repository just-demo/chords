package edu.self.types;

public enum Note {
	C("C"),C_("C#"),D("D"),D_("D#"),E("E"),F("F"),F_("F#"),G("G"),G_("G#"),A("A"),A_("A#"),B("B");
	private static final Note[] notes = values(); //does it make optimization???
	public static final int CIRCLE = notes.length;
	private String name;
	
	Note(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public Note getNext(){
		return getNext(1);
	}
	
	public Note getNext(int distance){
		int j = (this.ordinal() + distance) % CIRCLE;
		if (j < 0){
			j += CIRCLE;
		}
		return notes[j];
	}
	
	public int getDistance(Note note){
		int distance = note.ordinal() - this.ordinal();
		if (distance < 0){
			distance += CIRCLE;
		}
		return distance;
	}
}
