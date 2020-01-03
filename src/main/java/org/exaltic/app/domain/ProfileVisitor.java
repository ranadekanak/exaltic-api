package org.exaltic.app.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_profile_visitors")
public class ProfileVisitor extends AbstractIdentifierEntity {

	private User user;
	private Date viewDate;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "view_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getViewDate() {
		return viewDate;
	}
	
	public void setViewDate(Date viewDate) {
		this.viewDate = viewDate;
	}

}
