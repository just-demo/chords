package edu.self.web.form;

import org.springframework.stereotype.Component;

@Component
public class SearchForm {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
