package com.lehanh.pama.ui.patientcase;

interface GroupListener {

	void beforeExpanded(boolean expanded, boolean redraw, GalleryCustomGroupItem galleryCustomGroupItem);

	void afterExpanded(boolean expanded, boolean redraw, GalleryCustomGroupItem galleryCustomGroupItem);
}