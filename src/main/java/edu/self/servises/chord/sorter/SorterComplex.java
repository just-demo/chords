package edu.self.servises.chord.sorter;

import edu.self.servises.chord.Sorter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class SorterComplex implements Sorter {
    public List<Integer[]> sort(List<Integer[]> chords) {
        List<Chord> chordList = chords.stream()
                .map(Chord::new)
                .collect(toList());

        for (int i1 = 0; i1 < chordList.size(); i1++) {
            for (int i2 = i1 + 1; i2 < chordList.size(); i2++) {
                Chord chord1 = chordList.get(i1);
                Chord chord2 = chordList.get(i2);
                if (areSame(chord1, chord2)) {
                    chord1.same.add(chord2);
                }
            }

        }

        List<SameChords> sameChordGroups = new ArrayList<>();
        while (chordList.size() > 0) {
            List<Chord> same = getSameRecursively(chordList.iterator().next());
            sameChordGroups.add(new SameChords(same));
            chordList.removeAll(same);
        }

        List<Integer[]> chordsSorted = new ArrayList<>();
        sameChordGroups.sort(this::compare);
        sameChordGroups.forEach(sameChordGroup -> {
            List<Chord> same = sameChordGroup.chords;
            same.sort(this::compare);
            same.forEach(chord -> chordsSorted.add(chord.frets));
        });

        return chordsSorted;
    }

    private List<Chord> getSameRecursively(Chord chord) {
        List<Chord> collector = new ArrayList<>();
        collectSameRecursively(chord, collector);
        return collector;
    }

    private void collectSameRecursively(Chord chord, List<Chord> collector) {
        collector.add(chord);
        chord.same.forEach(same -> {
            if (!collector.contains(same)) {
                collectSameRecursively(same, collector);
            }
        });
    }

    private int compare(SameChords rc1, SameChords rc2) {
        if (rc1.minClosed != rc2.minClosed) {
            return rc1.minClosed > rc2.minClosed ? 1 : -1;
        }

        if (rc1.minAverage != rc2.minAverage) {
            return rc1.minAverage > rc2.minAverage ? 1 : -1;
        }
        return 0;
    }

    private int compare(Chord c1, Chord c2) {
        if (c1.open != c2.open) {
            return c1.open < c2.open ? 1 : -1;
        }

        if (c1.closed != c2.closed) {
            return c1.closed > c2.closed ? 1 : -1;
        }

        if (c1.average != c2.average) {
            return c1.average > c2.average ? 1 : -1;
        }
        return 0;
    }

    private boolean areSame(Chord chord1, Chord chord2) {
        int length = Math.min(chord1.frets.length, chord2.frets.length);
        for (int i = 0; i < length; i++) {
            Integer fret1 = chord1.frets[i];
            Integer fret2 = chord2.frets[i];
            if (fret1 != null && fret1 != 0 && fret2 != null && fret2 != 0 && !fret1.equals(fret2)) {
                return false;
            }
        }
        return true;
    }

    private class SameChords {
        List<Chord> chords;
        double minAverage;
        int minClosed;

        SameChords(List<Chord> chords) {
            this.chords = chords;
            this.minClosed = chords.stream()
                    .mapToInt(chord -> chord.closed)
                    .min()
                    .getAsInt();
            this.minAverage = chords.stream()
                    .mapToDouble(chord -> chord.average)
                    .min()
                    .getAsDouble();
        }
    }

    private class Chord {
        Integer[] frets;
        int open = 0;
        int closed;
        double average = 0;
        Set<Chord> same = new HashSet<>();

        public Chord(Integer[] frets) {
            this.frets = frets;
            double sum = 0;
            int count = 0;
            for (Integer fret : frets) {
                if (fret != null) {
                    if (fret == 0) {
                        open++;
                    }
                    sum += fret;
                    count++;
                }
            }
            closed = frets.length - count;
            if (count != 0) {
                average = sum / count;
            }
        }
    }
}
