package edu.self.servises.chord.filter;

import edu.self.servises.chord.Filter;

public class ClosedStringsUpFilter implements Filter {
	@Override
	public boolean accept(Integer[] frets) {
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
