package org.exaltic.app.dto;

import javax.validation.constraints.NotNull;

import org.exaltic.app.enums.AuthProvider;

public class RegistrationDTO {

	@NotNull
	private String name;
	@NotNull
	private String email;
	@NotNull
	private String imageUrl;
	@NotNull
	private AuthProvider provider;
	@NotNull
	private String deviceId;
	@NotNull
	private String deviceType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public AuthProvider getProvider() {
		return provider;
	}

	public void setProvider(AuthProvider provider) {
		this.provider = provider;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
