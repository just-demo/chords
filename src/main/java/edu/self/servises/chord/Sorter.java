package edu.self.servises.chord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sorter {
	public void sort(List<Integer[]> chords){
		List<Chord> chordsSortable = new ArrayList<Chord>();
		for (Integer[] frets: chords){
			chordsSortable.add(new Chord(frets));
		}
		Collections.sort(chordsSortable);
		int n = chords.size();
		for (int i = 0; i < n; ++i){
			chords.set(i, chordsSortable.get(i).frets);
		}
	}
	
	private class Chord implements Comparable<Chord>{
		private double average = 0;
		private int count = 0;
		private Integer[] frets;
		
		@Override
		public int compareTo(Chord chord) {
			if (count != chord.count){
				return count < chord.count ? 1 : -1;
			}
			
			if (average != chord.average){
				return average > chord.average ? 1 : -1;
			}
			return 0;
		}

		public Chord(Integer[] frets) {
			this.frets = frets;
			for (Integer fret: frets){
				if (fret != null){
					average += fret;
					++count;
				}
			}
			if (count != 0){
				average /= count;
			}
		}
	}
}
