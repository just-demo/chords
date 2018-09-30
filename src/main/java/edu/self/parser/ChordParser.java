package edu.self.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import edu.self.model.ChordCustom;

@Service
public class ChordParser {
	public static final String PATTERN = "^(x|\\d+)-(x|\\d+)-(x|\\d+)-(x|\\d+)-(x|\\d+)-(x|\\d+)$";
	private static final Pattern pattern = Pattern.compile(PATTERN);

	public String asString(List<Integer> frets) {
		StringBuffer buffer = new StringBuffer();
		for (Integer fret: frets){
			if (buffer.length() > 0){
				buffer.append("-");
			}
			buffer.append(asString(fret));
		}
		return buffer.toString();
	}

	public String asString(Integer ... frets) {
		return asString(Arrays.asList(frets));
	}

	public List<ChordCustom> readChords(String in) throws IOException{
		return readChords(new ByteArrayInputStream(in.getBytes()));
	}
	
	public List<ChordCustom> readChords(InputStream in) throws IOException{
		Properties chordDefinitions = new Properties();
		chordDefinitions.load(in);
		return readChords(chordDefinitions);
	}
	
	public List<ChordCustom> readChords(Properties chordDefinitions){
		List<ChordCustom> chords = new ArrayList<ChordCustom>();
		for(Object key : chordDefinitions.keySet()) {
			String chordName = (String) key;
			String frets = (String)chordDefinitions.getProperty(chordName);
			List<Integer> fretsList = asList(frets);
			if (fretsList == null){
				throw new Error("Incorrect line: " + frets);
			}
			ChordCustom chord = new ChordCustom(fretsList);
			chord.setName(chordName);
			chords.add(chord);
		}
		return chords;
	}

	public List<Integer> asList(String frets) {
		Matcher matcher = pattern.matcher(frets);
		if (matcher.find()){
			List<Integer> fretsList = new ArrayList<Integer>();
			for (int i = matcher.groupCount(); i >= 1; --i){
				fretsList.add(asInteger(matcher.group(i)));
			}
			return fretsList;
		}
		return null;
	}
	
	public Integer[] asArray(String frets) {
		return asList(frets).toArray(new Integer[]{});
	}
	
	public Integer asInteger(String fret){
		return "x".equals(fret) ? null : Integer.parseInt(fret);
	}
	
	public String asString(Integer fret){
		return fret == null ? "x" : fret.toString();
	}
}
