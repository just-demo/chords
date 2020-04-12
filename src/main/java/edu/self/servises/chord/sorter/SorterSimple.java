package edu.self.servises.chord.sorter;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SorterSimple implements Sorter {
	public List<Integer[]> sort(List<Integer[]> chords){
	    return chords.stream()
                .map(Chord::new)
                .sorted()
                .map(chord -> chord.frets)
                .collect(toList());
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
