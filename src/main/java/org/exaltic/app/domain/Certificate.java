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
@Table(name = "tbl_certs")
public class Certificate extends AbstractIdentifierEntity {

	private Trainer trainer;
	private String title;
	private String issuer;
	private Date issuedDate;
	private Date expiryDate;

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
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "issuer", nullable = false)
	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	@Column(name = "issued_date", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	@Column(name = "expiry_date")
	@Temporal(TemporalType.DATE)
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(expiryDate, issuedDate, issuer, title, trainer.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Certificate))
			return false;
		Certificate other = (Certificate) obj;
		return Objects.equals(expiryDate, other.expiryDate) && Objects.equals(issuedDate, other.issuedDate)
				&& Objects.equals(issuer, other.issuer) && Objects.equals(title, other.title)
				&& Objects.equals(trainer.getId(), other.trainer.getId());
	}

	

}
