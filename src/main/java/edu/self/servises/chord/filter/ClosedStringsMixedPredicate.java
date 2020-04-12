package edu.self.servises.chord.filter;

import java.util.function.Predicate;

public class ClosedStringsMixedPredicate implements Predicate<Integer[]> {
	@Override
	public boolean test(Integer[] frets) {
		boolean hasClosedAfterOpen = false;
		boolean hasOpen = false;
		for (Integer fret: frets){
			if (fret == null){
				if (hasOpen){
					hasClosedAfterOpen = true;
				}
			}
			else {
				if (hasClosedAfterOpen){
					return false;
				}
				hasOpen = true;
			}
		}
		return true;
	}
}
