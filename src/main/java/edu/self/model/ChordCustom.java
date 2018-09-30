package edu.self.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="chord")
public class ChordCustom {
	@Id
	@GeneratedValue
	@Column
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	private Integer string1;
	
	@Column
	private Integer string2;
	
	@Column
	private Integer string3;
	
	@Column
	private Integer string4;
	
	@Column
	private Integer string5;
	
	@Column
	private Integer string6;
	
	//@ManyToOne
	@JoinColumn(name="next")
	@OneToOne(fetch=FetchType.LAZY)
	private ChordCustom next;
	   
	public ChordCustom(){}
	
	public ChordCustom(List<Integer> frets){
		this.string1 = extract(frets, 0);
		this.string2 = extract(frets, 1);
		this.string3 = extract(frets, 2);
		this.string4 = extract(frets, 3);
		this.string5 = extract(frets, 4);
		this.string6 = extract(frets, 5);
	}
	
	private <T> T extract(List<T> values, int index){
		return values.size() > index ? values.get(index) : null;
	}
	
	public ChordCustom getNext() {
		return next;
	}

	public void setNext(ChordCustom next) {
		this.next = next;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getString1() {
		return string1;
	}

	public void setString1(Integer string1) {
		this.string1 = string1;
	}

	public Integer getString2() {
		return string2;
	}

	public void setString2(Integer string2) {
		this.string2 = string2;
	}

	public Integer getString3() {
		return string3;
	}

	public void setString3(Integer string3) {
		this.string3 = string3;
	}

	public Integer getString4() {
		return string4;
	}

	public void setString4(Integer string4) {
		this.string4 = string4;
	}

	public Integer getString5() {
		return string5;
	}

	public void setString5(Integer string5) {
		this.string5 = string5;
	}

	public Integer getString6() {
		return string6;
	}

	public void setString6(Integer string6) {
		this.string6 = string6;
	}
}
