package edu.self.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.self.dao.CustomChordDao;
import edu.self.model.ChordCustom;
import edu.self.parser.ChordParser;
import edu.self.web.editor.FretEditor;
import edu.self.web.form.ChordForm;
import edu.self.web.form.ChordUploadForm;

@Controller
@RequestMapping("/customChord")
//@SessionAttributes("chord")
public class CustomChordController {
	private static final String CHORD_POPUP = "chord/popup";
	private static final String CHORD_EDIT = "chord/edit";
	private static final String CHORD_UPLOAD = "chord/upload";
	private static final String CHORD_LIST = "chord/list";
	private static final String ERROR_404 = "error/404";
	
	@Inject
	private CustomChordDao chordDao;

	@Inject
	private FretEditor fretEditor;

	@Inject
	private ChordParser chordParser;

	@ModelAttribute("chord")
	private ChordForm createChordForm() {
		ChordForm chordForm = new ChordForm();
		return chordForm;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Integer.class, fretEditor);
	}
	
	@RequestMapping(value="/popup", method=RequestMethod.GET)
	public String popup(@RequestParam("name") String chordName, Model model){
		ChordCustom chord = chordDao.getChordByName(chordName);
		if (chord == null){
			return ERROR_404;
		}
		ChordForm chordForm = createChordForm();
		BeanUtils.copyProperties(chord, chordForm);
		model.addAttribute("chord", chordForm);
		return CHORD_POPUP;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/edit")
	public String edit(@ModelAttribute("chord") ChordForm chordForm) {
		return CHORD_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/edit") //params=parse is not checked because form can be submitted by JS
	public String parse(@ModelAttribute("chord") ChordForm chordForm) {
		return CHORD_EDIT;
	}
	
	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("chords", chordDao.getChords());
		return CHORD_LIST;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/edit", params = "save")
	public String save(@Valid @ModelAttribute("chord") ChordForm chordForm,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return CHORD_EDIT;
		}

		ChordCustom chord = new ChordCustom();
		BeanUtils.copyProperties(chordForm, chord);
		chordDao.addChord(chord);
		return CHORD_EDIT;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/upload")
	public String bulk(@ModelAttribute("chordUpload") ChordUploadForm chordUploadForm){
		return CHORD_UPLOAD;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/upload", params = "save")
	public String saveBulk(@ModelAttribute("chordUpload") ChordUploadForm chordUploadForm) {
		Map<String, ChordCustom> chordByName = new HashMap<String, ChordCustom>();
		//if (true) return "chord/upload";
		List<ChordCustom> chords;
		try{
			chords = chordParser.readChords(chordUploadForm.getChords());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (ChordCustom chord: chords){
			chordByName.put(chord.getName(), chord);
		}
		String[] transpositionLines = chordUploadForm.getTranspositions().split("\n");
		for (String transpositionLine: transpositionLines){
			String[] transpositionChords = transpositionLine.trim().split("\\s+");
			if (transpositionChords.length > 1){
				for (int i = 0; i < transpositionChords.length; ++i){
					int j = (i + 1) % transpositionChords.length;
					String currentChord = transpositionChords[i];
					String nextChord    = transpositionChords[j];
					if (chordByName.containsKey(currentChord) && chordByName.containsKey(nextChord)){
						chordByName.get(currentChord).setNext(chordByName.get(nextChord));
					}
				}
			}			
		}
		chordDao.addChords(chords);
		return CHORD_UPLOAD;
	}
	
	public ChordParser getChordParser() {
		return chordParser;
	}

	public void setChordParser(ChordParser chordParser) {
		this.chordParser = chordParser;
	}

	public FretEditor getFretEditor() {
		return fretEditor;
	}

	public void setFretEditor(FretEditor fretEditor) {
		this.fretEditor = fretEditor;
	}

	public CustomChordDao getChordDao() {
		return chordDao;
	}

	public void setChordDao(CustomChordDao chordDao) {
		this.chordDao = chordDao;
	}
}
