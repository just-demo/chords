package edu.self.dao;

import java.util.List;

import edu.self.model.Song;
import edu.self.model.SongSummary;
import edu.self.model.User;

public interface SongDao {
	public void deleteSong(Song song);
	public void addSong(Song song);
	public Song getSongById(Integer id);
	public void saveSong(Song song);
	public List<Song> getSongs();
	public int getSongsCount();
	public List<Song> getSongs(int from, int count);
	public void setSongs(List<Song> songs);
	public List<SongSummary> getSongSummaries();
	//TODO: combine the methods into one search method
	public List<Song> getSongsByPerformer(String performer);
	public List<Song> getSongsByTag(String tagName);
	public List<SongSummary> getSongSummariesByPerformer(String performer);
	public List<SongSummary> getSongSummariesByTag(String tag);
	public List<SongSummary> searchSongs(String songName, String performerName);
	public List<Song> searchSongs(String text);
}
