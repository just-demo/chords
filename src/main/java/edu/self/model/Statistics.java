package edu.self.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="statistics")
public class Statistics {
	@Id
	@GeneratedValue
	@Column
	private Integer id;
	
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="song_id")
	//@Column(name="entity_id")
	//private Integer entityId;

	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="entity_id")
	//private Song song;
	
	@Column(nullable = false)
	private Integer positiveRate = 0;
	
	@Column(nullable = false)
	private Integer negativeRate = 0;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	/*
	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	*/
	
	public Integer getPositiveRate() {
		return positiveRate;
	}
	
	/*
	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}
	*/

	public void setPositiveRate(Integer positiveRate) {
		this.positiveRate = positiveRate;
	}

	public Integer getNegativeRate() {
		return negativeRate;
	}

	public void setNegativeRate(Integer negativeRate) {
		this.negativeRate = negativeRate;
	}
}
