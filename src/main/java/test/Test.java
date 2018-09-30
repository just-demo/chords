package test;

import com.sun.xml.ws.transport.http.servlet.WSServletContextListener;
import edu.self.converters.SongXmlConverter;
import edu.self.model.Performer;
import edu.self.model.Song;
import edu.self.parser.SongParser;
import edu.self.servises.chord.ChordServiceImpl;
import edu.self.servises.chord.Filters;
import edu.self.servises.chord.Generator;
import edu.self.servises.chord.Sorter;
import edu.self.servises.chord.filter.ClosedStringsUpFilter;
import edu.self.servises.chord.filter.MaxWidthFilter;
import edu.self.servises.chord.filter.NaturalFilter;
import edu.self.soap.SongService;
import edu.self.types.Chord;
import edu.self.types.Note;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Service;
import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Test {
    private String s = "AAA";

    void go(int ouch) {

    }

    public static void main(String[] args) {
        int x = 0;
        System.out.println((x++ + x--) - (x-- + x++));
    }

    static void doStuff(Object... z) {

    }

    public static void main5(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:8080/m/soap/song?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://soap.self.edu/", "SongServiceImplService");

        Service service = Service.create(url, qname);

        SongService song = service.getPort(SongService.class);

        System.out.println(song.parseSimple("aaaa", "GGG"));
        //JAXRPCContextListener sdfsdf;
        WSServletContextListener dsdfdsf;
    }

    void test() {
        System.out.println(s);
        int s = 10;
        System.out.println(s);
        System.out.println(this.s);
    }

    public static void main4(String[] args) {
        int itemsCount = 99;
        int intemsPerPage = 10;
        double i = (int) Math.ceil((double) itemsCount / intemsPerPage);
        ;
        System.out.println(i);
    }

    public static void main3(String[] args) throws Exception {
        String inFileName = "D:\\songs.xml";
        String outFileName = "D:\\songs_out.xml";

        //FileWriter out = new FileWriter(outFileName);
        //OutputStream outputStream = new FileOutputStream(outFileName);
        PrintStream out = new PrintStream(outFileName, "UTF-8");

        File inFile = new File(inFileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inFile);
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
                //System.out.println(text);
                Song song = new Song();
                song.setName(name);
                Performer performer = new Performer();
                performer.setName(performerName);
                song.setPerformer(performer);
//				song.setText(songParser.parse(text));
//				song.setText(trimEmptyLines(text));
                song.setText(text);
                songs.add(song);
            }
        }

        SongXmlConverter songXmlConverter = new SongXmlConverter();
        String outXml = songXmlConverter.songsToXml(songs);
        System.out.println(outXml);
        out.println(outXml);
        out.close();
    }

    private static String trimEmptyLines(String text) {
        String[] lines = text.split("\n");
        StringBuffer buffer = new StringBuffer();
        boolean skipEmpty = true;
        for (String line : lines) {
            if (line.isEmpty()) {
                if (!skipEmpty) {
                    buffer.append(line + "\n");
                }
                skipEmpty = !skipEmpty;
            } else {
                buffer.append(line + "\n");
                skipEmpty = true;
            }
        }
        return buffer.toString();
    }

    public static void main2(String[] args) throws Exception {
/*
.noteF_{	background-color: c5007f;
.noteG_{	background-color: ff3f00;
.noteA_{	background-color: ffbf00;
 */
        //String from = "FF7F00";
        //String to = "FFFF00";
        //System.out.println(getMiddleColorHex(from, to));
        //System.out.println(getMiddleColorHex2(from, to));

        //if (true) return;

        Timer.start();

        Generator generator = new Generator();
        generator.setStrings(Note.E, Note.B, Note.G, Note.D, Note.A, Note.E);

        String[] chordNames = {"Dm"}; //"C C# D D# E F F# G G# A A# B Cm C#m Dm D#m Em Fm F#m Gm G#m Am A#m Bm C7 C#7 D7 D#7 E7 F7 F#7 G7 G#7 A7 A#7 B7";

        Filters filters = new Filters();
        filters.addFilter(new MaxWidthFilter());
        filters.addFilter(new ClosedStringsUpFilter());
        filters.addFilter(new NaturalFilter());

        Sorter sorter = new Sorter();

        for (String chordName : chordNames) {
            Note[] chordNotes = Chord.getChord(chordName).getNotes();
            generator.setNotes(chordNotes);
            List<Integer[]> chords = generator.getChords();
            System.out.println(chordName + ":" + chords.size());
            chords = filters.apply(chords);
            System.out.println(chordName + ":" + chords.size());

            sorter.sort(chords);
            /*
               for (Integer[] chord: chords){
                   System.out.println(makeString(chord));
               }
               //*/
        }

        ChordServiceImpl chordService = new ChordServiceImpl();
        Chord c = chordService.searchChord(new Integer[]{1, 3, 2, 0, 0, null});
        System.out.println(c.getName());
        /*
          generator.setNotes(Note.A, Note.C, Note.E);
          List<Integer[]> chords = generator.getChords();
          for (Integer[] chord: chords){
              System.out.println(makeString(chord));
          }
          */
        System.out.println(Timer.time());

        /*
          5-3-2-2-1-5
          5-3-2-5-1-5
          5-3-2-2-5-5
          5-3-7-5-5-5
          5-7-7-5-5-5
          8-7-7-5-5-5
          5-3-2-5-5-5
          8-7-7-9-5-5
          8-7-7-9-10-8
          8-7-10-9-10-8
          5-7-7-5-5-8
          8-7-7-5-5-8
          5-7-7-9-5-8
          8-7-7-9-5-8
          0-0-2-2-1-0
          0-3-2-2-1-0
          0.0070
          */
        //System.out.printf("Time= %.5f sec.", Timer.time());
    }


    private static String getMiddleColorHex(String from, String to) {
        StringBuffer middleColorHex = new StringBuffer();
        for (int i = 0; i < 3; ++i) {
            int fromPart = Integer.parseInt(from.substring(i * 2, (i + 1) * 2), 16);
            int toPart = Integer.parseInt(to.substring(i * 2, (i + 1) * 2), 16);
            String middleColorHexPart = Integer.toString((fromPart + toPart) / 2, 16);
            while (middleColorHexPart.length() < 2) {
                middleColorHexPart = "0" + middleColorHexPart;
            }
            middleColorHex.append(middleColorHexPart);
        }
        return middleColorHex.toString();
    }

    private static String getMiddleColorHex2(String from, String to) {
        int fromPart = Integer.parseInt(from, 16);
        int toPart = Integer.parseInt(to, 16);
        return Integer.toString((fromPart + toPart) / 2, 16);
    }

    private static String makeString(Integer[] frets) {
        StringBuffer buffer = new StringBuffer();
        for (int i = frets.length - 1; i >= 0; --i) {
            buffer.append(frets[i] == null ? "x" : frets[i]);
            if (i != 0) {
                buffer.append("-");
            }
        }
        return buffer.toString();
    }
}
