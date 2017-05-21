package com.lehanh.pama.patientcase;

public interface IImageInfo {

	String getDate();

	Number getId();

	//String generateImageName(long patientId, int rootId, int detailId, String surgerySymbol);
	String getFolderName();
	
	String getImageName();

}
