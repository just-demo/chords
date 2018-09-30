package edu.self.web.tags;

import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Functions {
	private static String encodeFret(Integer fret) {
		return fret == null ? "x" : fret.toString();
	}

	public static String chordToString(Object chord) {
		final BeanWrapper src = new BeanWrapperImpl(chord);
		StringBuffer buffer = new StringBuffer();
		for (int i = 6; i >= 1; --i) {
			if (i != 6) {
				buffer.append("-");
			}
			String propertyName = "string" + i;
			if (src.isReadableProperty(propertyName)) {
				buffer.append(encodeFret((Integer) src
						.getPropertyValue(propertyName)));
			}
		}
		return buffer.toString();
	}

	public static String fretsToString(Integer[] frets) {
		StringBuffer buffer = new StringBuffer();
		for (int i = frets.length - 1; i >= 0; --i) {
			buffer.append(encodeFret(frets[i]));
			if (i != 0) {
				buffer.append("-");
			}
		}
		return buffer.toString();
	}

	public static String fretsListToString(List<Integer> frets) {
		StringBuffer buffer = new StringBuffer();
		if (frets != null) {
			for (int i = frets.size() - 1; i >= 0; --i) {
				buffer.append(encodeFret(frets.get(i)));
				if (i != 0) {
					buffer.append("-");
				}
			}
		}
		return buffer.toString();
	}
}
