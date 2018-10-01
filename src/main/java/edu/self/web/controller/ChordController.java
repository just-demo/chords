package edu.self.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.self.dao.ChordDao;
import edu.self.servises.chord.ChordService;
import edu.self.servises.chord.UnsupportedChordException;
import edu.self.types.Chord;
import edu.self.types.GuitarString;
import edu.self.types.Note;
import edu.self.types.Type;
import edu.self.web.editor.FretEditor;
import edu.self.web.form.ChordForm;

@Component
@RequestMapping("/chord")
public class ChordController {
	private static final String CHORD_POPUP = "chord/popup";
	private static final String CHORD_VIEW = "chord/view";
	private static final String CHORD_EDIT = "chord/edit";
	private static final String CHORD_LIST = "chord/list";
	private static final String ERROR_404 = "error/404";

	@Inject
	@Named("chordServiceCustomized")
	private ChordService chordService;

	@Inject
	private FretEditor fretEditor;

	@Inject
	private ChordDao chordDao;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Integer.class, fretEditor);
	}

	@ModelAttribute("chord")
	public ChordForm createChordForm() {
		ChordForm chordForm = new ChordForm();
		Integer[] strings = new Integer[GuitarString.STRINGS_COUNT];
		Arrays.fill(strings, 0);
		chordForm.setStrings(strings);
		return chordForm;
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "selectChord", required = false) Boolean selectChord, @ModelAttribute("chord") ChordForm chordForm, Model model) {
		Chord chord;
		if (selectChord != null && chordForm.getChordNoteIndex() != null && chordForm.getChordTypeIndex() != null) {
			// TODO: check index range
			chord = Chord.getChord(Note.values()[chordForm.getChordNoteIndex()], Type.values()[chordForm.getChordTypeIndex()]);
			try {
				Integer[] strings = chordService.getChord(chord.getName());
				chordForm.setStrings(Arrays.copyOf(strings, GuitarString.STRINGS_COUNT));
				chordForm.setName(chord.getName());
			} catch (UnsupportedChordException e) {
			}
		} else {
			chord = chordService.searchChord(chordForm.getStrings());
			chordForm.setName(chord != null ? chord.getName() : "");
			chordForm.setChordNoteIndex(chord != null ? chord.getNote().ordinal() : null);
			chordForm.setChordTypeIndex(chord != null ? chord.getType().ordinal() : null);
		}

		Set<Note> currentNotes = chordService.getNotes(chordForm.getStrings());

		model.addAttribute("notes", Note.values());
		model.addAttribute("types", Type.values());
		model.addAttribute("currentNotes", currentNotes);
		return CHORD_EDIT;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/default")
	public String setDefault(@RequestParam("name") String chordName, @RequestParam("frets") String chordFrets, Principal principal) {
		if (principal != null) {
			chordDao.setFrets(principal.getName(), chordName, chordFrets);
		}

		try {
			chordName = URLEncoder.encode(chordName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// TODO: avoid the hard-coded url below:
		return "redirect:/" + CHORD_VIEW + "?name=" + chordName;
	}

	@RequestMapping(value = "/popup", method = RequestMethod.GET)
	public String popup(@RequestParam("name") String chordName, Model model) {
		Integer[] chord;
		try {
			chord = chordService.getChord(chordName);
		} catch (UnsupportedChordException e) {
			return ERROR_404;
		}
		model.addAttribute("chord", chord);
		return CHORD_POPUP;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(@RequestParam("name") String chordName, Model model) {
		Chord chord;
		List<Integer[]> chordVariants;
		Integer[] defaultChord;
		try {
			chord = Chord.getChord(chordName);
			chordVariants = chordService.getChords(chordName);
			defaultChord = chordService.getChord(chordName);
		} catch (UnsupportedChordException e) {
			return ERROR_404;
		}
		model.addAttribute("chord", chord);
		sortChordVariants(chordVariants, defaultChord);
		model.addAttribute("chordVariants", chordVariants);
		model.addAttribute("defaultChord", defaultChord);
		return CHORD_VIEW;
	}

	private void sortChordVariants(List<Integer[]> chordVariants, final Integer[] defaultChord) {
		if (defaultChord != null) {
			chordVariants.stream().filter(chord -> Arrays.equals(chord, defaultChord)).findFirst().ifPresent(
					chord -> Collections.swap(chordVariants, chordVariants.indexOf(chord), 0)
			);
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, Principal principal) {
		/*
		Map<String, Integer[]> chords = new HashMap<String, Integer[]>();
		Set<String> chordNames = chordService.getChordNames();
		for (String chordName : chordNames) {
			try {
				Integer[] frets = chordService.getChord(chordName);
				chords.put(chordName, frets);
			} catch (UnsupportedChordException e) {

			}
		}
		*/

		List<Map<String, Integer[]>> chords = new ArrayList<Map<String, Integer[]>>();
		for (Note note: Note.values()){
			Map<String, Integer[]> chordsOfNote = new LinkedHashMap<String, Integer[]>();
			chords.add(chordsOfNote);
			for (Type type: Type.values()){
				Chord chord = Chord.getChord(note, type);
				try {
					Integer[] frets = chordService.getChord(chord.getName());
					chordsOfNote.put(chord.getName(), frets);
				} catch (UnsupportedChordException e) {

				}
			}
		}

		model.addAttribute("chords", chords);
		return CHORD_LIST;
	}

	public FretEditor getFretEditor() {
		return fretEditor;
	}

	public void setFretEditor(FretEditor fretEditor) {
		this.fretEditor = fretEditor;
	}

	public ChordService getChordService() {
		return chordService;
	}

	public void setChordService(ChordService chordService) {
		this.chordService = chordService;
	}

	public ChordDao getChordDao() {
		return chordDao;
	}

	public void setChordDao(ChordDao chordDao) {
		this.chordDao = chordDao;
	}
}