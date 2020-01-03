package org.exaltic.app.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_attributes")
public class Attribute extends AbstractIdentifierEntity {

	private Trainer trainer;
	private String name;
	private Double value;
	private String unit;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "trainer_id", referencedColumnName = "id", nullable = false)
	public Trainer getTrainer() {
		return trainer;
	}
	
	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, trainer.getId(), unit, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Attribute))
			return false;
		Attribute other = (Attribute) obj;
		return Objects.equals(name, other.name) && Objects.equals(trainer.getId(), other.trainer.getId())
				&& Objects.equals(unit, other.unit) && Objects.equals(value, other.value);
	}
	
	

}
