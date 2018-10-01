package edu.self.dao;

import java.util.List;

import edu.self.model.Song;
import edu.self.model.SongSummary;
import edu.self.model.User;

public interface SongDao {
	void deleteSong(Song song);
	Song getSongById(Integer id);
	void saveSong(Song song);
	List<Song> getSongs();
	int getSongsCount();
	List<Song> getSongs(int from, int count);
	void setSongs(List<Song> songs);
	//TODO: combine the methods into one search method
	List<Song> getSongsByPerformer(String performer);
	List<Song> getSongsByTag(String tagName);
	List<Song> searchSongs(String text);
}
