package edu.self.servises.chord.filter;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;


public class Filter {
    private Predicate<Integer[]>[] predicates;

    @SafeVarargs
    public Filter(Predicate<Integer[]>... predicates) {
        this.predicates = predicates;
    }

    public List<Integer[]> apply(List<Integer[]> chords) {
        return chords.stream()
                .filter(this::accept)
                .collect(toList());
    }

    private boolean accept(Integer[] frets) {
        return stream(predicates).allMatch(filter -> filter.test(frets));
    }
}
