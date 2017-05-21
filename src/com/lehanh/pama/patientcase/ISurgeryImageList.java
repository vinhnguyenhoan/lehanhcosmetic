package com.lehanh.pama.patientcase;

import java.text.ParseException;
import java.util.List;

public interface ISurgeryImageList {

	Long getPatientId();
	
	void iteratorCaseDetail(ICaseDetailHandler caseDetailHandler) throws ParseException;

	void deletedImages(List<String> imageNameToDelete);

	IImageInfo addImage(String filePath, int groupId, int detailId, String sugeryName, String extension);

}
