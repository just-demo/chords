package edu.self.servises.chord.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static edu.self.utils.ChordUtils.getDeviation;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;


public class Filter {
    private Predicate<Integer[]>[] predicates;
    @SafeVarargs
    public Filter(Predicate<Integer[]>... predicates) {
        this.predicates = predicates;
    }

    public List<Integer[]> apply(List<Integer[]> chords) {
        chords = applyPredicates(chords);
//        chords = filterSame(chords);
        return chords;
    }

    private List<Integer[]> applyPredicates(List<Integer[]> chords) {
        return chords.stream()
                .filter(frets -> stream(predicates).allMatch(filter -> filter.test(frets)))
                .collect(toList());
    } 
    
    private List<Integer[]> filterSame(List<Integer[]> chords) {
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

        List<Integer[]> filterResult = new ArrayList<>();
        while (chordList.size() > 0) {
            List<Chord> same = getSameRecursively(chordList.iterator().next());
            chordList.removeAll(same);
            same.sort(this::compare);
            filterResult.add(same.get(0).frets);
        }
        return filterResult;
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

    private int compare(Chord c1, Chord c2) {
        int diff = -Integer.compare(c1.open, c2.open);
        if (diff != 0) {
            return diff;
        }

        diff = Integer.compare(c1.closed, c2.closed);
        if (diff != 0) {
            return diff;
        }

        diff = Double.compare(c1.deviation, c2.deviation);
        if (diff != 0) {
            return diff;
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

    private class Chord {
        Integer[] frets;
        double deviation;
        int open;
        int closed;
        Set<Chord> same = new HashSet<>();

        public Chord(Integer[] frets) {
            this.frets = frets;
            deviation = getDeviation(frets);
            for (Integer fret : frets) {
                if (fret == null) {
                    closed++;
                } else if (fret == 0) {
                    open++;
                }
            }
        }
    }
}
