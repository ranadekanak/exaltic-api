package org.exaltic.app.domain;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_categories")
public class Category extends AbstractIdentifierEntity {

	@Column(name = "name")
	private String name;
	
    private Collection<Trainer> trainers = new HashSet<>();

	public Category() {
	}

	public Category(String name) {
		this.enabled = Boolean.TRUE;
		this.version = 1;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            },
            mappedBy = "categories")
	public Collection<Trainer> getTrainers() {
		return trainers;
	}
	
	public void setTrainers(Collection<Trainer> trainers) {
		this.trainers = trainers;
	}

}
