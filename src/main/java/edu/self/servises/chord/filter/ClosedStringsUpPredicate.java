package edu.self.servises.chord.filter;

import java.util.function.Predicate;

public class ClosedStringsUpPredicate implements Predicate<Integer[]> {
	@Override
	public boolean test(Integer[] frets) {
		boolean hasClosed = false;
		for (Integer fret: frets){
			if (fret == null){
				hasClosed = true;
			}
			else if (hasClosed){
				return false;
			}
		}
		return true;
	}
}
