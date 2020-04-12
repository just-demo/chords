package edu.self.servises.chord.filter;

import java.util.function.Predicate;

public class NaturalPredicate implements Predicate<Integer[]> {
	private int maxFingersCount;
	public NaturalPredicate(){
		this(4);
	}
	
	public NaturalPredicate(int maxFingersCount){
		this.maxFingersCount = maxFingersCount;
	}
	
	@Override
	public boolean test(Integer[] frets) {
		int min = Integer.MAX_VALUE;
		for (Integer fret: frets){
			if (fret != null && fret != 0 && fret < min){
				min = fret;
			}
		}
		boolean minAsOne = true;
		boolean hasMin = false;
		
		int allCount = 0;
		int minCount = 0;
		
		for (Integer fret: frets){
			if (fret != null){
				if (fret == min){
					++minCount;
					hasMin = true;
				}
				else if (fret > min){
					++allCount;
				}
				else if (fret == 0){
					if (hasMin){
						minAsOne = false;
					}
				}
			}
		}
		allCount += minAsOne ? 1 : minCount;
		return allCount <= maxFingersCount;
	}
}