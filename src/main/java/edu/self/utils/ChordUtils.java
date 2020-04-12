package edu.self.utils;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ChordUtils {
    public static double getDeviation(Integer[] frets) {
        double sum = 0;
        int count = 0;
        for (Integer fret : frets) {
            if (fret != null) {
                sum += fret;
                count++;
            }
        }
        int bar = getBarOrZero(frets);
        return count == 0 ? 0 : sum / count - bar;
    }

    public static Integer getBarOrZero(Integer[] frets){
        return ofNullable(getBar(frets)).orElse(0);
    }

    public static Integer getBar(Integer[] frets){
        boolean hasClosed = false;
        Integer bar = null;
        for (Integer fret: frets){
            if (fret == null){
                hasClosed = true;
            } else if (hasClosed){
                // closed are allowed only on top
                return null;
            } else if (bar == null || fret < bar) {
                bar = fret;
            }
        }
        return bar;
    }

    public static List<String> formatFrets(List<Integer[]> fretNums) {
        return fretNums.stream()
                .map(frets -> stream(frets)
                        .map(fret -> fret == null ? "x" : fret.toString())
                        .collect(joining("-")))
                .collect(toList());
    }
}
