package edu.self.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.self.model.Tag;

public interface TagDao {
	Tag getTagById(Integer id);
	Tag getTagByName(String name);
	List<Tag> getTags();
	Set<Tag> prepareTags(Collection<Tag> tags);
	Tag prepareTag(String name);
}
