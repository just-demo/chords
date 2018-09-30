package edu.self.dao;

public interface ChordDao {
	String getFrets(String userName, String chordName);
	void setFrets(String userName, String chordName, String frets);
}
