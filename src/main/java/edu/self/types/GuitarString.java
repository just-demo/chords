package edu.self.types;

public enum GuitarString {
	ONE(Note.E), TWO(Note.B), THREE(Note.G), FOUR(Note.D), FIVE(Note.A), SIX(Note.E);
	private static final GuitarString[] strings = values(); //TODO: does it make any optimization???
	public static final int STRINGS_COUNT = strings.length;
	private Note note;
	GuitarString(Note note){
		this.note = note;
	}
	
	public static GuitarString getString(int number){
		return number > STRINGS_COUNT ? null : strings[number - 1];
	}
	
	public Note getNote(){
		return note;
	}
}
