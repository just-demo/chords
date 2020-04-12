package edu.self.servises.chord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class Sorter2 {
    // TODO: there are duplicated produced and the result is not good enough
    public List<Integer[]> sort(List<Integer[]> chords) {
        List<ChordWithRelated> chordWithRelated = chords.stream()
                .map(ChordWithRelated::new)
                .collect(toList());

        new ArrayList<>(chordWithRelated).forEach(superSet ->
                new ArrayList<>(chordWithRelated).forEach(subSet -> {
                    if (superSet != subSet && superSet.isRelated(subSet)) {
                        superSet.addRelated(subSet);
                    }
                }));

        List<RelatedChords> relatedChords = new ArrayList<>();
        while (chordWithRelated.size() > 0) {
            Set<ChordWithRelated> related = chordWithRelated.iterator().next().getRelatedRecursively();
            List<Chord> chords2 = related.stream().map(chord -> new Chord(chord.frets)).collect(toList());
            relatedChords.add(new RelatedChords(chords2));
            chordWithRelated.removeAll(related);
        }

        List<Integer[]> chordsSorted = new ArrayList<>();
        relatedChords.sort(this::compare);
        relatedChords.forEach(related -> {
            List<Chord> chords2 = related.chords;
            chords2.sort(this::compare);
            chords2.forEach(chord -> chordsSorted.add(chord.frets));
        });

        return chordsSorted;
    }

    private boolean isSubSet(Integer[] superSet, Integer[] subSet) {
        if (subSet.length > superSet.length) {
            return false;
        }
        for (int i = 0; i < subSet.length; i++) {
            Integer subItem = subSet[i];
            Integer superItem = superSet[i];
            if (subItem != null && !subItem.equals(0) && !subItem.equals(superItem)) {
                return false;
            }
        }
        return true;
    }

    private int compare(RelatedChords rc1, RelatedChords rc2) {
        if (rc1.maxCount != rc2.maxCount) {
            return rc1.maxCount < rc2.maxCount ? 1 : -1;
        }

        if (rc1.minAverage != rc2.minAverage) {
            return rc1.minAverage > rc2.minAverage ? 1 : -1;
        }
        return 0;
    }


    private int compare(Chord c1, Chord c2) {
        if (isSubSet(c1.frets, c2.frets)) {
            return 1;
        }

        if (isSubSet(c2.frets, c1.frets)) {
            return -1;
        }

        if (c1.count != c2.count) {
            return c1.count < c2.count ? 1 : -1;
        }

        if (c1.average != c2.average) {
            return c1.average > c2.average ? 1 : -1;
        }
        return 0;
    }

    private class RelatedChords {
        private List<Chord> chords;
        private double minAverage;
        private int maxCount;

        public RelatedChords(List<Chord> chords) {
            this.chords = chords;
            this.maxCount = chords.stream()
                    .mapToInt(chord -> chord.count)
                    .max()
                    .getAsInt();
            this.minAverage = chords.stream()
                    .mapToDouble(chord -> chord.average)
                    .min()
                    .getAsDouble();
        }
    }

    private class ChordWithRelated {
        private Integer[] frets;
        private Set<ChordWithRelated> related = new HashSet<>();

        public ChordWithRelated(Integer[] frets) {
            this.frets = frets;
        }

        public void addRelated(ChordWithRelated chord) {
            related.add(chord);
        }

        public boolean isRelated(ChordWithRelated subSet) {
            return isSubSet(frets, subSet.frets);
        }

        public Set<ChordWithRelated> getRelatedRecursively() {
            Set<ChordWithRelated> result = new HashSet<>();
            collectRelatedRecursively(result);
            return result;
        }

        private void collectRelatedRecursively(Set<ChordWithRelated> collector) {
            collector.add(this);
            related.forEach(chord -> {
                if (!collector.contains(chord)) {
                    chord.collectRelatedRecursively(collector);
                }
            });
        }
    }

    private class Chord {
        private double average = 0;
        private int count = 0;
        private Integer[] frets;

        public Chord(Integer[] frets) {
            this.frets = frets;
            for (Integer fret : frets) {
                if (fret != null) {
                    average += fret;
                    ++count;
                }
            }
            if (count != 0) {
                average /= count;
            }
        }
    }
}
