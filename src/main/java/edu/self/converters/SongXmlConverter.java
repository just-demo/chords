package edu.self.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.self.model.Performer;
import edu.self.model.Song;
import edu.self.parser.SongParser;
import edu.self.servises.chord.ChordServiceImpl;

@Service
public class SongXmlConverter extends XmlConverter {
	public static void main(String[] args) throws Exception {
		SongXmlConverter songXmlConverter = new SongXmlConverter();
		//System.out.println(songXmlConverter.songsToXml(null));
		System.out.println(songXmlConverter.xmlToSongs(null).size());
	}

	public String songsToXml(List<Song> songs) throws ParserConfigurationException, TransformerException, UnsupportedEncodingException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement("songs");
		for (Song song : songs) {
			Element songElement = document.createElement("song");

			Element name = document.createElement("name");
			Element performer = document.createElement("performer");
			Element text = document.createElement("text");

			name.appendChild(document.createTextNode(song.getName()));
			performer.appendChild(document.createTextNode(song.getPerformer().getName()));
			text.appendChild(document.createCDATASection(song.getText()));

			songElement.appendChild(name);
			songElement.appendChild(performer);
			songElement.appendChild(text);

			rootElement.appendChild(songElement);
		}
		document.appendChild(rootElement);
		return documentToXml(document);
	}
	
	public List<Song> xmlToSongs(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		//String inFileName = "D:\\songs.xml";
		
		//File inFile = new File(inFileName);
		//InputStream in = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		//Document doc = db.parse(inFile);
		Document doc = db.parse(in);
		doc.getDocumentElement().normalize();
		
		NodeList songNodes = doc.getElementsByTagName("song");
		List<Song> songs = new ArrayList<Song>();
		SongParser songParser = new SongParser();
		ChordServiceImpl chordService = new ChordServiceImpl();
		songParser.setChordNames(chordService.getChordNames());
		for (int s = 0; s < songNodes.getLength(); s++) {
			Node songNode = songNodes.item(s);
			if (songNode.getNodeType() == Node.ELEMENT_NODE) {
				String name = ((Element) ((Element) songNode).getElementsByTagName("name").item(0)).getChildNodes().item(0).getNodeValue();
				String performerName = ((Element) ((Element) songNode).getElementsByTagName("performer").item(0)).getChildNodes().item(0).getNodeValue();
				String text = ((Element) ((Element) songNode).getElementsByTagName("text").item(0)).getChildNodes().item(0).getNodeValue();
				Song song = new Song();
				song.setName(name);
				Performer performer = new Performer();
				performer.setName(performerName);
				song.setPerformer(performer);
				song.setText(text);
				songs.add(song);
			}
		}
		return songs;
	}
}
