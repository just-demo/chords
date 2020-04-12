package edu.self.servises.chord;

import org.testng.annotations.Test;

import java.util.List;

import static edu.self.utils.ChordUtils.formatFrets;

public class ChordServiceTest {
    private ChordService chordService = new ChordServiceImpl();

    @Test
    public void testGetChords() {
        List<Integer[]> chords = chordService.getChords("F");
        System.out.println(formatFrets(chords));
    }
}