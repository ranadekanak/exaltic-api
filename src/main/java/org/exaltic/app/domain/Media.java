package org.exaltic.app.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.exaltic.app.enums.MediaType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_media")
public class Media extends AbstractIdentifierEntity {

	private Trainer trainer;
	private String name;
	private String description;
	private String path;
	private MediaType type;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Enumerated(EnumType.STRING)
	public MediaType getType() {
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name, path, trainer.getId(), type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Media))
			return false;
		Media other = (Media) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& Objects.equals(path, other.path) && Objects.equals(trainer.getId(), other.trainer.getId())
				&& Objects.equals(type, other.type);
	}

	
}
