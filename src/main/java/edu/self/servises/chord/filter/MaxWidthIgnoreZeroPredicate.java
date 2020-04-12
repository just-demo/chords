package edu.self.servises.chord.filter;

import java.util.function.Predicate;

public class MaxWidthIgnoreZeroPredicate implements Predicate<Integer[]> {
	private int maxWidth;
	public MaxWidthIgnoreZeroPredicate(){
		this(4);
	}
	
	public MaxWidthIgnoreZeroPredicate(int maxWidth){
		this.maxWidth = maxWidth;
	}
	
	@Override
	public boolean test(Integer[] frets) {
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
