package edu.self.web.form;

import org.springframework.stereotype.Component;

@Component
public class ChordUploadForm {
	private String chords;
	private String transpositions;
	
	public String getChords() {
		return chords;
	}
	public void setChords(String chords) {
		this.chords = chords;
	}
	public String getTranspositions() {
		return transpositions;
	}
	public void setTranspositions(String transpositions) {
		this.transpositions = transpositions;
	}
	
}
