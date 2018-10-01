package edu.self.servises.chord;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.self.dao.ChordDao;
import edu.self.parser.ChordParser;

@Service
public class ChordServiceCustomized extends ChordServiceImpl {
	@Inject
	private ChordDao chordDao;

	@Inject
	private ChordParser chordParser;
	
	public Integer[] getChord(String chordName) throws UnsupportedChordException {
		String chord = getUserChord(chordName);
		if (chord != null){
			return chordParser.asArray(chord);
		}
		return super.getChord(chordName);
	}

	/**
	 * user detail could be retrieved by "Principal" action parameter and then
	 * principal.getName()
	 */
	private String getUserChord(String chordName) {
		// TODO: fix security
		if (false && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			// anonymousUser is returned for not logged in users
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			// the same as ...getName()
			// String principal =
			// (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return chordDao.getFrets(userName, chordName);
		}
		return null;
	}
}
