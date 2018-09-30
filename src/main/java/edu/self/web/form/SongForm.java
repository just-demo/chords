package edu.self.web.form;

import java.util.Set;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Component
public class SongForm {
	private Integer id;
	@Size(min=1, max=255, message="Song name must be from 1 to 255 characters long")
	private String name;
	private String text;
	@Size(min=1, max=255, message="Song performer must be from 1 to 255 characters long")
	private String performerName;
	private String textInitial;
	//@Min(value=1, message="Song must not be empty")
	@NotEmpty(message="Song must not be empty")
	private String textProcess;
	private Set<Integer> rollBack;
	private String performerNameSelected;
	private String tags;
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getPerformerNameSelected() {
		return performerNameSelected;
	}
	public void setPerformerNameSelected(String performerNameSelected) {
		this.performerNameSelected = performerNameSelected;
	}
	public String getTextProcess() {
		return textProcess;
	}
	public void setTextProcess(String textProcess) {
		this.textProcess = textProcess;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPerformerName() {
		return performerName;
	}
	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}
	public Set<Integer> getRollBack() {
		return rollBack;
	}
	public void setRollBack(Set<Integer> rollBack) {
		this.rollBack = rollBack;
	}
	public String getTextInitial() {
		return textInitial;
	}
	public void setTextInitial(String textInitial) {
		this.textInitial = textInitial;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
