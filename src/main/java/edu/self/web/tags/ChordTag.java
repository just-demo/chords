package edu.self.web.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import edu.self.types.GuitarString;

public class ChordTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	protected String frets;
	protected String type;
	protected String id;
	protected String styleClass;

	protected Integer min;
	protected Integer max;
	protected Integer size;

	/*
	private void initChordParser(){
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
		chordParser = (ChordParser)wac.getBean("chordParser");
	}
	*/
	
	public String getStyleClass() {
		return styleClass;
	}
	
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getFrets() {
	   return this.frets;
	}
	
	public void setFrets(String frets) {
	   this.frets = frets;
	}

	public int doStartTag() throws JspException {
		JspWriter out;
		try {
			out = pageContext.getOut();
			if (frets != null && !frets.isEmpty()){
				String chordSchema = getChordSchema(frets);
				out.print(chordSchema);
			}
		} catch (IOException e) {
			throw new JspException("I/O Error : " + e.getMessage());
		}
		return Tag.SKIP_BODY;
	}
	
	private String getChordSchema(String fretsString){
		if (StringUtils.isBlank(fretsString)){
			return "";
		}
		List<Integer> frets = new ArrayList<Integer>();
		for (String fret: fretsString.split("-")){
			frets.add(decodeFret(fret));
		}
		Collections.reverse(frets);
		
		Integer minFret = Collections.min(frets, FretComparator.MIN);
		Integer maxFret = Collections.max(frets, FretComparator.MAX);
		
		Integer bar = hasBar(frets) ? minFret : null;
		
		minFret = ObjectUtils.defaultIfNull(minFret, 0);
		maxFret = ObjectUtils.defaultIfNull(maxFret, 1);

		int maxFretLength = Math.max(maxFret.toString().length(), 3);
        
		//expand min/max to -1/+1
        minFret = Math.max(0, minFret - 1);
        maxFret = Math.max(maxFret + 1, minFret + (size == null ? 0 : size) - (minFret > 0 ? 1 : 0));
        
        //expand min/max with tag attributes
        if (min != null && min >= 0 && min < minFret){
        	minFret = min;
        }
        if (max != null && max >= 0 && max > maxFret){
        	maxFret = max;
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\""
        		+ (id != null ? (" id=\"" + id + "\"") : "")
        		+ (styleClass != null ? (" class=\"" + styleClass + "\"") : "")
        		+ ">");
        if (type == "edit") {
        	buffer.append(makeBarLine(minFret, maxFret));
        }
        int string = 1;
        for (Integer fret: frets){
        	buffer.append("<tr note=\"" + getNoteName(string, fret) + "\">");
            buffer.append(makeFretCell(string, null, StringUtils.leftPad(encodeFret(fret), maxFretLength, " ").replaceAll(" ", "&nbsp;")));
            buffer.append(getLine(string, minFret, maxFret, fret, bar, maxFretLength));
            buffer.append("</tr>");
            buffer.append("\n");
            ++string;
        }
        buffer.append("</table>");
        return buffer.toString();
    }
	
	private String makeBarLine(Integer minFret, Integer maxFret){
        List<String> parts = new ArrayList<String>();
        parts.add(makeBarCell(0));
        for (Integer i = minFret; i <= maxFret; ++i){
        	if (i != 0){
	            parts.add(makeBarCell(i));
        	}
        }
        String delimiter = "<td>&nbsp;</td>";
        return "<tr class=\"bars\">" + StringUtils.join(parts, delimiter) + delimiter + "</tr>";
	}
	
	private String makeBarCell(Integer fret){
		return "<td><button type=\"button\" value=\"" + fret + "\">" + fret + "</button></td>";
	}
	
	private String getNoteName(int string, Integer fret){
		GuitarString guitarString = GuitarString.getString(string);
		String note = "X";
		if (guitarString != null && fret != null){
			note = guitarString.getNote().getNext(fret).getName();
		}
		return note;
	}
	
	private String getStringName(int string){
		GuitarString guitarString = GuitarString.getString(string);
		String note = "X";
		if (guitarString != null){
			note = guitarString.getNote().getName();
		}
		return note;
	}
	
	private boolean hasBar(List<Integer> frets){
		boolean hasClosed = false;
		for (Integer fret: frets){
			if (fret == null){
				hasClosed = true;
			} else if (hasClosed){
				// closed are allowed only on top
				return false;
			}
		}
		return true;
	}
	
	private String makeFretCell(Integer string, Integer fret, String body){
		return "<td title=\"" + getNoteName(string, fret != null ? fret : 0) + "\" class=\"target\" string=\"" + string + "\" fret=\"" + (fret == null ? "" : String.valueOf(fret)) + "\">" + body + "</td>";
	}
	
	private String encodeFret(Integer fret){
		return fret == null ? "x" : String.valueOf(fret);
	}
	
	public Integer decodeFret(String fret){
		return "x".equals(fret) ? null : Integer.valueOf(fret);
	}

    private String getLine(int string, int minFret, int maxFret, Integer fret, Integer bar, int maxFretLength){
        List<String> parts = new ArrayList<String>();

        for (Integer i = minFret; i <= maxFret; ++i){
        	if (i != 0){
	        	String body = StringUtils.center(i.equals(fret) || i.equals(bar) ? (fret != null ? String.valueOf(i) : "x") : "-", maxFretLength, "-");
	            parts.add(makeFretCell(string, i, body));
        	}
        }
        
        String delimiter = "<td>|</td>";

        return delimiter + StringUtils.join(parts, delimiter) + delimiter;
    }
}
