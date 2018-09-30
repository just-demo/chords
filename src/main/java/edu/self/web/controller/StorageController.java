package edu.self.web.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.self.converters.ChordXmlConverter;
import edu.self.converters.SongXmlConverter;
import edu.self.dao.SongDao;
import edu.self.model.Song;
import edu.self.servises.chord.ChordService;
import edu.self.servises.chord.UnsupportedChordException;
import edu.self.types.Chord;

@Controller
@RequestMapping("/storage")
public class StorageController {
	private static final String STORAGE = "storage";

	@Inject
	private SongDao songDao;

	@Inject
	@Named("chordServiceCustomized")
	private ChordService chordService;

	@Inject
	SongXmlConverter songXmlConverter;

	@Inject
	ChordXmlConverter chordXmlConverter;

	@RequestMapping("")
	public String index() {
		return STORAGE;
	}

	@RequestMapping(value = "/song", params = "export")
	public void exportSongs(HttpServletResponse response) {

		List<Song> songs = songDao.getSongs();

		// return SONG_EXPORT_INDEX;
		try {
			// response.setContentType("text/plain");
			response.setContentType("text/xml"); // "; charset=UTF-8");
			// response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=songs.xml");
			PrintWriter out = response.getWriter();

			String fileContent = songXmlConverter.songsToXml(songs);
			out.write(fileContent);

			// response.setContentType("application/pdf");
			// response.setHeader("Content-Disposition",
			// "attachment; filename=somefile.pdf");
			// OutputStream out = response.getOutputStream();
			// out.write('a');
			response.flushBuffer();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//@RequestMapping(method = RequestMethod.POST, value = "/song")
	@RequestMapping(method = RequestMethod.POST, value = "/song", params = "import")
	public String importSongs(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			//System.out.println(file.getContentType());
			try {
				List<Song> songs = songXmlConverter.xmlToSongs(file.getInputStream());
				songDao.setSongs(songs);
				//System.out.println(songs);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return STORAGE;
	}

	@RequestMapping(value = "/chord", params = "export")
	public void exportChords(HttpServletResponse response) {
		Map<Chord, Integer[]> chords = new HashMap<Chord, Integer[]>();

		for (Chord chord : Chord.getChords()) {
			try {
				Integer[] frets = chordService.getChord(chord.getName());
				chords.put(chord, frets);
			} catch (UnsupportedChordException e) {
			}
		}

		try {
			response.setContentType("text/xml");
			response.setHeader("Content-Disposition", "attachment; filename=chords.xml");
			PrintWriter out = response.getWriter();

			String fileContent = chordXmlConverter.chordsToXml(chords);
			out.write(fileContent);
			response.flushBuffer();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
