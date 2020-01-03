package org.exaltic.app.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class AbstractIdentifierEntity {

	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "is_enabled")
	protected Boolean enabled;

	@Version
	@Column(name = "version", nullable = false)
	protected Integer version;

	@Id
	@GenericGenerator(name = "tbl_id_generator", strategy = "org.hibernate.id.enhanced.TableGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled"),
			@org.hibernate.annotations.Parameter(name = "initial_value", value = "1000000"),
			@org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
			@org.hibernate.annotations.Parameter(name = "prefer_entity_table_as_segment_value", value = "true") })
	@GeneratedValue(generator = "tbl_id_generator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public Integer getVersion() {
		return version == null ? 1 : version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@JsonIgnore
	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}
