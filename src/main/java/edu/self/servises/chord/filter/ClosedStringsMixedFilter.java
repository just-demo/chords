package edu.self.servises.chord.filter;

import edu.self.servises.chord.Filter;

public class ClosedStringsMixedFilter implements Filter {
	@Override
	public boolean accept(Integer[] frets) {
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
