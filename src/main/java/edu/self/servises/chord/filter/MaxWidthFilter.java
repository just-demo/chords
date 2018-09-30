package edu.self.servises.chord.filter;

import edu.self.servises.chord.Filter;

public class MaxWidthFilter implements Filter {
	private int maxWidth;
	public MaxWidthFilter(){
		this(4);
	}
	
	public MaxWidthFilter(int maxWidth){
		this.maxWidth = maxWidth;
	}
	
	@Override
	public boolean accept(Integer[] frets) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (Integer fret: frets){
			if (fret != null){
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
