package edu.self.converters;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.self.servises.chord.UnsupportedChordException;
import edu.self.types.Chord;

@Service
public class ChordXmlConverter extends XmlConverter{
	public static void main(String[] args) throws Exception {
		ChordXmlConverter converter = new ChordXmlConverter();
		Map<Chord, Integer[]> chords = new HashMap<Chord, Integer[]>();
		try{
			chords.put(Chord.getChord("Am"), new Integer[]{0,0,2,2,1,0});
		}
		catch (UnsupportedChordException e) {}
		System.out.println(converter.chordsToXml(chords));
	}

	public String chordsToXml(Map<Chord, Integer[]> chords) throws ParserConfigurationException, TransformerException, UnsupportedEncodingException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement("chords");
		Set<Chord> chordSet = chords.keySet();
		for (Chord chord: chordSet) {
			Integer[] chordFrets = chords.get(chord);
			
			Element chordElement = document.createElement("chord");
			
			Element name  = document.createElement("name");
			Element next  = document.createElement("next");
			Element frets = document.createElement("frets");

			name.appendChild(document.createTextNode(chord.getName()));
			next.appendChild(document.createTextNode(chord.getNext().getName()));
			for (Integer chordFret: chordFrets){
				Element fret = document.createElement("fret");
				fret.appendChild(document.createTextNode(chordFret == null ? "" : chordFret.toString()));
				frets.appendChild(fret);
			}
			chordElement.appendChild(name);
			chordElement.appendChild(next);
			chordElement.appendChild(frets);
			
			rootElement.appendChild(chordElement);
		}
		document.appendChild(rootElement);
		return documentToXml(document);
	}
}
