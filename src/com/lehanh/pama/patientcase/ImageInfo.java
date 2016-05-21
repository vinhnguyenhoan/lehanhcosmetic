package com.lehanh.pama.patientcase;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lehanh.pama.IJsonDataObject;

public class ImageInfo implements IJsonDataObject {

	private String imagePath;
	
	@Expose
	@SerializedName("pros")
	private Map<String, Object> properties;
	
	ImageInfo() {
	}
	
	ImageInfo(String imagePath) {
		this.imagePath = imagePath;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
}
