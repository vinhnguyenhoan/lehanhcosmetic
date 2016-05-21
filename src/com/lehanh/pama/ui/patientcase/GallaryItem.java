package com.lehanh.pama.ui.patientcase;

import java.util.Date;
import java.util.List;

import com.lehanh.pama.catagory.Catagory;

class GalleryItemData implements Comparable<GalleryItemData> {

	String folder;
	String imageName;
	String surSym;
	String date;
	int groupId;
	int caseDetailId;
	List<Catagory> allSurCat;
	Date examDate;
	int afterDays;
	
	GalleryItemData(String folder, String surSym, String date, String imageName, int groupId, int caseDetailId,
			Date examDate, int afterDays, List<Catagory> allSurCat) {
		this.folder = folder;
		this.imageName = imageName;
		this.surSym = surSym;
		this.date = date;
		this.afterDays = afterDays;
		this.groupId = groupId;
		this.caseDetailId = caseDetailId;
		this.examDate = examDate;
		this.allSurCat = allSurCat;
	}

	GalleryItemData(int groupId, int caseDetailId, Date examDate, int afterDays) {
		this.groupId = groupId;
		this.caseDetailId = caseDetailId;
		this.examDate = examDate;
		this.afterDays = afterDays;
	}

	@Override
	public int compareTo(GalleryItemData o) {
		if (o == null) {
			return 0;
		}
		if (this.groupId == o.groupId) {
			return Integer.valueOf(this.caseDetailId).compareTo(o.caseDetailId);
		}
		return Integer.valueOf(this.groupId).compareTo(o.groupId);
	}

	String fullPath() {
		return this.folder + this.imageName;
	}
	
}