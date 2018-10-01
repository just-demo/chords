package edu.self.dao;

import edu.self.model.User;

public interface UserDao {
	User getUserByName(String username);
	void saveUser(User user);
}
