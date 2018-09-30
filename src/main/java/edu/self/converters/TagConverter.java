package edu.self.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import edu.self.model.Tag;

@Service
public class TagConverter {
	private static final String DELIMITER = ",";
	public String tagsToString(Collection<Tag> tags){
		Collection<String> tagNames = new ArrayList<String>();
		if (tags != null){
			for (Tag tag: tags){
				tagNames.add(tag.getName());
			}
		}
		return StringUtils.join(tagNames, DELIMITER + " ");
	}
	
	public Set<Tag> stringToTags(String tagNames){
		Set<Tag> tags = new HashSet<Tag>();
		String[] tagsNamesArray = tagNames.split(DELIMITER);
		for (String name: tagsNamesArray){
			if (StringUtils.isNotBlank(name)){
				Tag tag = new Tag();
				tag.setName(StringUtils.trim(name));
				tags.add(tag);
			}
		}
		return tags;
	}
}
