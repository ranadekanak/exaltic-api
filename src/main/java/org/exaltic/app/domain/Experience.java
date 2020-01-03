package org.exaltic.app.domain;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_work_history")
public class Experience extends AbstractIdentifierEntity {

	private Trainer trainer;
	private String designation;
	private String company;
	private Date fromDate;
	private Date toDate;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "trainer_id", referencedColumnName = "id", nullable = false)
	public Trainer getTrainer() {
		return trainer;
	}
	
	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}
	
	@Column(name = "title", nullable = false)
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Column(name = "company", nullable = false)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "issued_date", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	@Column(name = "expiry_date")
	@Temporal(TemporalType.DATE)
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(company, designation, fromDate, toDate, trainer.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Experience))
			return false;
		Experience other = (Experience) obj;
		return Objects.equals(company, other.company) && Objects.equals(designation, other.designation)
				&& Objects.equals(fromDate, other.fromDate) && Objects.equals(toDate, other.toDate)
				&& Objects.equals(trainer.getId(), other.trainer.getId());
	}
	
	

}
