package org.exaltic.app.dto;

import org.exaltic.app.enums.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class MultiMediaUploadRequest {

	private MultipartFile file;
	private String name;
	private String description;
	private String path;
	private MediaType type;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getName() {
		return name == null ? file.getOriginalFilename() : name;
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

	public MediaType getType() {
		if(type == null) {
			String extension = file.getOriginalFilename().split("\\.")[1];
			type = MediaType.valueOf(extension);
		}
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}

}
