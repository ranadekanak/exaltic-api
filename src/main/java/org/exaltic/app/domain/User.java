package org.exaltic.app.domain;


import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.exaltic.app.enums.AuthProvider;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends AbstractIdentifierEntity {

	private String firstName;
	private String lastName;
	private String email;
	private String imageUrl;
    private AuthProvider provider;
    private Collection<Device> devices = new HashSet<Device>();

    @JsonIgnore
    @OneToMany(targetEntity = Device.class, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	public Collection<Device> getDevices() {
		return devices;
	}
    
    public void setDevices(Collection<Device> devices) {
		this.devices = devices;
	}
    
	@Column(name = "first_name", nullable = false)
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "last_name", nullable = false)
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Email
	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "image_url", nullable = false)
	public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    @Column(name = "auth_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }


}
