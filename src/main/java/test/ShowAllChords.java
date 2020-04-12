package test;

import edu.self.servises.chord.ChordService;
import edu.self.servises.chord.ChordServiceImpl;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static edu.self.utils.ChordUtils.formatFrets;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

public class ShowAllChords {
    public static void main(String[] args) throws Exception {
        ChordService chordService = new ChordServiceImpl();
        List<String> names = chordService.getChordNames().stream().sorted().collect(toList());
        Map<String, String> transposition = chordService.getTranspositionTable(1);
        List<Chord> chords = names.stream()
                .map(name -> new Chord(
                        name,
                        transposition.get(name),
                        formatFrets(chordService.getChords(name))))
                .collect(toList());
        writeFile("src/main/webapp/static/data/chords.json", toJson(chords));
    }

    private static String toJson(Object object) throws IOException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    private static void writeFile(String file, String content) throws IOException {
        Files.createDirectories(Paths.get(file).getParent());
        FileUtils.writeStringToFile(new File(file), content, UTF_8);
    }

    private static class Chord {
        String name;
        String next;
        List<String> frets;

        public Chord(String name, String next, List<String> frets) {
            this.name = name;
            this.next = next;
            this.frets = frets;
        }

        public String getName() {
            return name;
        }

        public String getNext() {
            return next;
        }

        public List<String> getFrets() {
            return frets;
        }
    }
}
