package edu.self.web.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.self.bean.PageScroller;
import edu.self.converters.TagConverter;
import edu.self.dao.PerformerDao;
import edu.self.dao.SongDao;
import edu.self.dao.TagDao;
import edu.self.dao.UserDao;
import edu.self.dao.VoteDao;
import edu.self.model.Performer;
import edu.self.model.Song;
import edu.self.model.SongSummary;
import edu.self.model.Statistics;
import edu.self.model.Tag;
import edu.self.model.User;
import edu.self.parser.SongParser;
import edu.self.servises.chord.ChordService;
import edu.self.servises.chord.UnsupportedChordException;
import edu.self.web.form.SearchForm;
import edu.self.web.form.SongForm;

@Controller
@RequestMapping("/song")
public class SongController {
	// private static final String EDIT = "edit";
	private static final String LIST = "list";
	private static final String SONG_SEARCH = "song/search";
	private static final String SONG_VIEW = "song/view";
	private static final String SONG_EDIT = "song/edit";
	private static final String SONG_LIST = "song/list";
	private static final String ERROR_404 = "error/404";
	
	private static final int SONGS_PER_PAGE = 20;
	private static final int PAGE_SCROLLER_LINKS = 10;

	@Inject
	private SongDao songDao;

	@Inject
	private VoteDao voteDao;

	@Inject
	private UserDao userDao;

	@Inject
	@Named("chordServiceCustomized")
	private ChordService chordService;

	@Inject
	private PerformerDao performerDao;

	@Inject
	private TagDao tagDao;

	@Inject
	private SongParser songParser;

	@Inject
	private TagConverter tagConverter;

	@PostConstruct
	public void initParser() {

	}

	@ModelAttribute("songs")
	// default would be songList if we do not specify "songs"
	public List<Song> getSongs() {
		return songDao.getSongs();
	}

	@ModelAttribute("song")
	// in order to prevent exception:
	// org.springframework.web.HttpSessionRequiredException: Session attribute
	// 'songForm' required - not found in session
	public SongForm createSongForm() {
		return new SongForm();
	}

	@ModelAttribute("performers")
	public List<Performer> createPerformers() {
		return performerDao.getPerformers();
	}

	@ModelAttribute("tags")
	public List<Tag> createTags() {
		return tagDao.getTags();
	}

	@ModelAttribute("searchForm")
	public SearchForm createSearchForm() {
		return new SearchForm();
	}
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\\
	@RequestMapping(method = RequestMethod.GET, value = "/view/{id}")
	public String view(Principal principal, @PathVariable("id") Integer id, @RequestParam(value = "transposition", required = false) Integer transposition, Model model) {
		Song song = songDao.getSongById(id);
		if (song == null) {
			return ERROR_404;
		}
		SongForm songForm = createSongForm();
		BeanUtils.copyProperties(song, songForm, new String[] { "tags" });
		if (song.getPerformer() != null) {
			songForm.setPerformerName(song.getPerformer().getName());
		}

		if (transposition != null) {
			songParser.setTranspositionTable(chordService.getTranspositionTable(transposition));
			songForm.setText(songParser.transpose(songForm.getText()));
		}

		Map<String, Integer[]> chords = new HashMap<String, Integer[]>();
		Collection<String> chordNames = songParser.getChordNames(songForm.getText());
		for (String chordName : chordNames) {
			try {
				Integer[] chord = chordService.getChord(chordName);
				chords.put(chordName, chord);
			} catch (UnsupportedChordException e) {

			}
		}
		boolean canVote = principal != null ? !voteDao.voted(song, userDao.getUserByName(principal.getName())) : false;

		model.addAttribute("song", songForm);
		model.addAttribute("transposition", ObjectUtils.defaultIfNull(transposition, 0));
		model.addAttribute("chords", chords);
		model.addAttribute("statistics", song.getStatistics());
		model.addAttribute("mayVote", canVote);

		return SONG_VIEW;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		Song song = songDao.getSongById(id);
		if (song == null) {
			return ERROR_404;
		}
		songDao.deleteSong(song);
		return "redirect:/" + SONG_LIST;
	}

	// @PreAuthorize("hasRole('ROLE_USER')")
	@Secured("ROLE_USER")
	@RequestMapping(method = RequestMethod.POST, value = "/vote/{songId}")
	public @ResponseBody
	Map<String, String> vote(Principal principal, @PathVariable("songId") Integer songId, @RequestParam("rate") Integer rate, Model model) {
		Map<String, String> response = new HashMap<String, String>();

		Song song = songDao.getSongById(songId);
		if (song == null) {
			response.put("status", "404");
		} else {
			User user = userDao.getUserByName(principal.getName());
			Statistics statistics = song.getStatistics();
			if (statistics == null) {
				statistics = new Statistics();
			}

			if (!voteDao.voted(song, user)) {
				if (rate < 0) {
					statistics.setNegativeRate(statistics.getNegativeRate() + 1);
				} else {
					statistics.setPositiveRate(statistics.getPositiveRate() + 1);
				}

				song.setStatistics(statistics);
				songDao.saveSong(song);
				voteDao.setVote(song, user, rate);
			}

			response.put("positiveRate", String.valueOf(statistics.getPositiveRate()));
			response.put("negativeRate", String.valueOf(statistics.getNegativeRate()));
			response.put("status", "200");
		}

		return response;
	}

	@RequestMapping("/list")
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, Model model) {
		//List<SongSummary> songs = songDao.getSongSummaries();
		//sortSongSummariesByPerformer(songs);
		List<Song> songs = songDao.getSongs((page - 1) * SONGS_PER_PAGE, SONGS_PER_PAGE);
		//List<Song> songs = songDao.getSongs();
		sortSongsByPerformer(songs);
		model.addAttribute("songs", songs);
		model.addAttribute("pageScroller", new PageScroller(songDao.getSongsCount(), page, SONGS_PER_PAGE, PAGE_SCROLLER_LINKS));
		return SONG_LIST;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, params = "search")
	public String listSearch(@ModelAttribute("searchForm") SearchForm searchForm, Model model) {
		List<Song> songs;
		if (!StringUtils.isBlank(searchForm.getText())){
			songs = songDao.searchSongs(searchForm.getText());
			//List<SongSummary> songs = songDao.searchSongs(songForm.getName(), songForm.getPerformerName());
			//sortSongsByPerformer(songs);
		}
		else {
			return "redirect:/" + SONG_LIST;
		}
		model.addAttribute("songs", songs);
		return SONG_LIST;
	}
	
	private void sortSongSummariesByPerformer(List<SongSummary> songs){
		Collections.sort(songs, new Comparator<SongSummary>() {
			@Override
			public int compare(SongSummary o1, SongSummary o2) {
				String p1 = o1.getPerformer();
				String p2 = o2.getPerformer();
				if (p1 != null && p2 != null) {
					return p1.compareTo(p2);
				}
				return p1 != null ? 1 : (p2 != null ? -1 : 0);
			}
		});
	}
	
	private void sortSongsByPerformer(List<Song> songs){
		Collections.sort(songs, new Comparator<Song>() {
			@Override
			public int compare(Song o1, Song o2) {
				String p1 = o1.getPerformer().getName();
				String p2 = o2.getPerformer().getName();
				if (p1 != null && p2 != null) {
					return p1.compareTo(p2);
				}
				return p1 != null ? 1 : (p2 != null ? -1 : 0);
			}
		});
	}

	@RequestMapping(value = "/list", params = "performer")
	public String listOfPerformer(HttpServletRequest request, @RequestParam("performer") String performer, Model model) {
		try {
			// the problem could be solved by modifying server.xml config file
			// (see http://wiki.apache.org/tomcat/FAQ/CharacterEncoding#Q2)
			performer = new String(performer.getBytes("ISO8859-1"), "UTF-8");
		} catch (Exception e) {
		}
		//TODO: change to songs
		List<Song> songs = songDao.getSongsByPerformer(performer);
		model.addAttribute("songs", songs);
		return SONG_LIST;
	}

	@RequestMapping(value = "/list", params = "tag")
	public String listByTag(HttpServletRequest request, @RequestParam("tag") String tag, Model model) {
		try {
			// the problem could be solved by modifying server.xml config file
			// (see http://wiki.apache.org/tomcat/FAQ/CharacterEncoding#Q2)
			tag = new String(tag.getBytes("ISO8859-1"), "UTF-8");
		} catch (Exception e) {
		}
		//TODO: change to songs
		List<Song> songs = songDao.getSongsByTag(tag);
		model.addAttribute("songs", songs);
		return SONG_LIST;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/edit")
	public String process(@RequestParam(value = "parse", required = false) Boolean parse, @RequestParam(value = "transpose", required = false) Integer transposition,
			@ModelAttribute("song") SongForm songForm) {
		songParser.setChordNames(chordService.getChordNames());
		if (parse != null) {
			songForm.setTextProcess(songParser.parse(songForm.getTextInitial()));
		}
		if (transposition != null) {
			songParser.setTranspositionTable(chordService.getTranspositionTable(transposition));
			songForm.setTextProcess(songParser.transpose(songForm.getTextProcess(), songForm.getRollBack()));
		}
		songForm.setText(songParser.rollBack(songForm.getTextProcess(), songForm.getRollBack()));
		return SONG_EDIT;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/edit")
	public String edit(Model model) {
		SongForm songForm = createSongForm();
		model.addAttribute("song", songForm);
		return SONG_EDIT;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/edit/{id}")
	public String editExistent(@PathVariable("id") Integer id, Model model) {
		SongForm songForm = createSongForm();

		Song song = songDao.getSongById(id);
		if (song == null) {
			return ERROR_404;
		}
		BeanUtils.copyProperties(song, songForm, new String[] { "tags" });
		if (song.getPerformer() != null) {
			songForm.setPerformerName(song.getPerformer().getName());
			songForm.setPerformerNameSelected(song.getPerformer().getName());
		}
		songForm.setTextInitial(song.getText());
		songForm.setTags(tagConverter.tagsToString(song.getTags()));
		model.addAttribute("song", songForm);

		// TODO: try to trim {id} from url path
		return process(true, null, songForm);
		// return SONG_EDIT;
	}

	// TODO: also secure save method - 1) in case of editing an existent song;
	// 2) in case of saving a new song
	@RequestMapping(method = RequestMethod.POST, value = "/edit", params = "save")
	public String save(@Valid @ModelAttribute("song") SongForm songForm, BindingResult bindingResult) {
		songForm.setText(songParser.rollBack(songForm.getTextProcess(), songForm.getRollBack()));
		if (bindingResult.hasErrors()) {
			return SONG_EDIT;
		}

		Song song = new Song();
		BeanUtils.copyProperties(songForm, song, new String[] { "tags" });
		Performer performer = performerDao.preparePerformer(songForm.getPerformerName());
		song.setPerformer(performer);
		song.setTags(tagConverter.stringToTags(songForm.getTags()));

		// songDao.addSong(song);
		songDao.saveSong(song);
		// redirect in order to refresh song list
		return "redirect:" + LIST;
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\\
	public SongParser getSongParser() {
		return songParser;
	}

	public void setSongParser(SongParser songParser) {
		this.songParser = songParser;
	}

	/*
	 * public void test() { //it works!!! Connection conn = null;
	 * PreparedStatement stmt = null; try { conn = dataSource.getConnection();
	 * stmt = conn.prepareStatement(SQL_INSERT_SPITTER); stmt.setString(1,
	 * "name2"); stmt.setString(2, "description2"); stmt.execute(); } catch
	 * (SQLException e) { // dosomething...notsurewhat,though } finally { try {
	 * if (stmt != null) { stmt.close(); } if (conn != null) { conn.close(); } }
	 * catch (SQLException e) { // I'mevenlesssureaboutwhattodohere } } }
	 */
}