package edu.self.web.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.stereotype.Component;

@Component
public class FretEditor extends PropertyEditorSupport{
    public void setAsText(String text) {
    	Integer value = null;
    	if (text != null && text.matches("^\\d+$")){
    		value = Integer.valueOf(text);
    	}
        setValue(value);
    }

	@Override
	public String getAsText() {
		if (getValue() == null){
			return ""; 
		}
		return super.getAsText();
	}
}