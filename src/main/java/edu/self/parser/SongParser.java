package edu.self.parser;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class SongParser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String searchPattern;
	private String replacePattern;

	private static final Pattern preparedChordPattern = Pattern
			.compile("\\{([^}]+)\\}");

	// @Inject
	private Set<String> chordNames;
	private Map<String, String> transpositionTable;

	public Map<String, String> getTranspositionTable() {
		return transpositionTable;
	}

	public void setTranspositionTable(Map<String, String> transpositionTable) {
		this.transpositionTable = transpositionTable;
	}

	public Set<String> getChordNames() {
		return chordNames;
	}

	public void setChordNames(Set<String> chordNames) {
		this.chordNames = chordNames;
		init();
	}

	// @PostConstruct //it works, but we set chordNames manually
	public void init() {
		StringBuffer chordBuffer = new StringBuffer();
		for (String chord : chordNames) {
			// TODO: examine special characters in chords
			if (chordBuffer.length() != 0) {
				chordBuffer.append("|");
			}
			chordBuffer.append(chord);
		}
		// Lookbehind (?=\\s|$) is used instead of (\\s|$) in order to solve
		// problem with subsequent chords like "Am Em"
		searchPattern = "(^|\\s|\\()(" + chordBuffer + ")(?=\\s|\\)|$)";
		replacePattern = "$1{$2}";
	}

	public String parse(String text) {
		return text.replaceAll(searchPattern, replacePattern);
	}

	public String transpose(String text) {
		return transpose(text, null);
	}

	public String transpose(String text, Set<Integer> rollBack) {
		if (transpositionTable != null && !transpositionTable.isEmpty()) {
			Matcher mathcer = preparedChordPattern.matcher(text);
			StringBuffer buffer = new StringBuffer();
			int index = 0;
			while (mathcer.find()) {
				if (rollBack == null || !rollBack.contains(index)) {
					String chordName = mathcer.group(1);
					if (transpositionTable.containsKey(chordName)) {
						mathcer.appendReplacement(buffer, "{"
								+ transpositionTable.get(chordName) + "}");
					}
				}
				index++;
			}
			mathcer.appendTail(buffer);
			text = buffer.toString();
		}
		return text;
	}

	public String rollBack(String text, Set<Integer> rollBack) {
		if (rollBack != null && !rollBack.isEmpty()) {
			Matcher mathcer = preparedChordPattern.matcher(text);
			StringBuffer buffer = new StringBuffer();
			int index = 0;
			while (mathcer.find()) {
				if (rollBack.contains(index)) {
					mathcer.appendReplacement(buffer, "$1");
				}
				index++;
			}
			mathcer.appendTail(buffer);
			text = buffer.toString();
		}
		return text;
	}

	public Collection<String> getChordNames(String text) {
		Collection<String> chordNames = new HashSet<String>();
		Matcher mathcer = preparedChordPattern.matcher(text);
		while (mathcer.find()) {
			String chordName = mathcer.group(1);
			chordNames.add(chordName);
		}
		return chordNames;
	}
}
