package edu.self.web.tags;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class SongTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	/*
	 * public int doStartTag() throws JspException { JspWriter out;
	 * 
	 * try { out = pageContext.getOut(); out.print( "HHHHH" ); } catch(
	 * IOException e ) { throw new JspException( "I/O Error : " + e.getMessage()
	 * ); }
	 * 
	 * return Tag.SKIP_BODY;
	 * 
	 * }
	 */
	private static final Pattern pattern = Pattern.compile("\\{([^}]+)\\}");

	public int doAfterBody() throws JspException {
		try {
			BodyContent bodyContent = getBodyContent();
			String songText = bodyContent.getString();
			JspWriter out = bodyContent.getEnclosingWriter();
			if (songText != null) {
				songText = songText.replaceAll(" ", "&nbsp;")
						//.replaceAll("\\{([^}]+)\\}", "<span class=\"chord\">$1</span>")
						.replaceAll("\\n", "<br/>")
						;

				Matcher mathcer = pattern.matcher(songText);
				StringBuffer buffer = new StringBuffer();
				int index = 0;
				while (mathcer.find()) {
					mathcer.appendReplacement(buffer, "<span class=\"chord\" index=\"" + index + "\">$1</span>");
					index++;
				}
				mathcer.appendTail(buffer);
				songText = buffer.toString();	
				
				out.print(songText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
