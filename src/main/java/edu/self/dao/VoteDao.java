package edu.self.dao;

import edu.self.model.Song;
import edu.self.model.User;

public interface VoteDao {
	Boolean voted(Song song, User user);
	void setVote(Song song, User user, Integer voteValue);
}
