package edu.self.servises.chord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Filters {
	private Set<Filter> filters;
	public void addFilter(Filter filter){
		filters.add(filter);
	}
	
	public Filters(){
		filters = new HashSet<Filter>();
	}
	
	public boolean accept(Integer[] frets){
		for (Filter filter: filters){
			if (!filter.accept(frets)){
				return false;
			}
		}
		return true;
	}
	
	public List<Integer[]> apply(List<Integer[]> chords){
		List<Integer[]> result = new ArrayList<Integer[]>();
		for (Integer[] frets: chords){
			if (accept(frets)){
				result.add(frets);
			}
		}
		return result;
	}
}
