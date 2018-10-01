package edu.self.dao;

import java.util.List;

import edu.self.model.Performer;

public interface PerformerDao {
	Performer getPerformerByName(String name);
	Performer preparePerformer(String name);
	Performer preparePerformer(Performer performer);
	List<Performer> getPerformers();
}
