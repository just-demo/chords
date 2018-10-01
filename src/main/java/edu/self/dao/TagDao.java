package edu.self.dao;

import edu.self.model.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TagDao {
    Tag getTagByName(String name);

    List<Tag> getTags();

    Set<Tag> prepareTags(Collection<Tag> tags);

    Tag prepareTag(String name);
}
