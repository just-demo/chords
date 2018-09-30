package edu.self.web.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Component
public class ChordForm {
	//private static final String PATTERN = "^(x|\\d+)$";
	
	@NotEmpty(message = "Chord name is required")
	private String name;
	private Integer[] strings;
	private Integer chordNoteIndex;
	private Integer chordTypeIndex;
	
	public Integer getChordNoteIndex() {
		return chordNoteIndex;
	}

	public void setChordNoteIndex(Integer chordNoteIndex) {
		this.chordNoteIndex = chordNoteIndex;
	}

	public Integer getChordTypeIndex() {
		return chordTypeIndex;
	}

	public void setChordTypeIndex(Integer chordTypeIndex) {
		this.chordTypeIndex = chordTypeIndex;
	}

	public Integer[] getStrings() {
		return strings;
	}
	
	public void setStrings(Integer[] strings) {
		this.strings = strings;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
}
