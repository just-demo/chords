package edu.self.servises.chord.filter;

import edu.self.servises.chord.Filter;

public class MaxWidthIgnoreZeroFilter implements Filter {
	private int maxWidth;
	public MaxWidthIgnoreZeroFilter(){
		this(4);
	}
	
	public MaxWidthIgnoreZeroFilter(int maxWidth){
		this.maxWidth = maxWidth;
	}
	
	@Override
	public boolean accept(Integer[] frets) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (Integer fret: frets){
			if (fret != null && fret != 0){
				if (fret < min){
					min = fret;
				}
				if (fret > max){
					max = fret;
				}
			}
		}
		return max - min < maxWidth;
	}
}
