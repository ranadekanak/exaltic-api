package org.exaltic.app.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_trainers")
public class Trainer extends User {

	private String title;
	private String profile;
	private Collection<Attribute> attributes = new HashSet<Attribute>();
	private Collection<Certificate> certificates = new HashSet<Certificate>();
	private Collection<Experience> workHistory = new HashSet<Experience>();
	private Collection<ProfileVisitor> profileVisitors = new ArrayList<ProfileVisitor>();
	private Collection<Category> categories = new HashSet<>();
	private Boolean isPremium;

	@Column(name = "title", nullable = true)
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "profile", nullable = true)
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@OneToMany(targetEntity = Attribute.class, cascade = CascadeType.ALL, mappedBy = "trainer", orphanRemoval = true)
	public Collection<Attribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Collection<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	@OneToMany(targetEntity = Certificate.class, cascade = CascadeType.ALL, mappedBy = "trainer", orphanRemoval = true)
	public Collection<Certificate> getCertificates() {
		return certificates;
	}
	
	public void setCertificates(Collection<Certificate> certificates) {
		this.certificates = certificates;
	}
	
	@OneToMany(targetEntity = Experience.class, cascade = CascadeType.ALL, mappedBy = "trainer", orphanRemoval = true)
	public Collection<Experience> getWorkHistory() {
		return workHistory;
	}

	public void setWorkHistory(Collection<Experience> workHistory) {
		this.workHistory = workHistory;
	}

	@OneToMany(targetEntity = ProfileVisitor.class, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	public Collection<ProfileVisitor> getProfileVisitors() {
		return profileVisitors;
	}

	public void setProfileVisitors(Collection<ProfileVisitor> profileVisitors) {
		this.profileVisitors = profileVisitors;
	}
	
	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
    @JoinTable(name = "tbl_trainer_categories_mapping",
            joinColumns = { @JoinColumn(name = "trainer_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") })
	public Collection<Category> getCategories() {
		return categories;
	}
	
	public void setCategories(Collection<Category> categories) {
		this.categories = categories;
	}

	@Column(name = "is_premium", nullable = true)
	public Boolean getIsPremium() {
		return isPremium;
	}
	
	public void setIsPremium(Boolean isPremium) {
		this.isPremium = isPremium;
	}

}
