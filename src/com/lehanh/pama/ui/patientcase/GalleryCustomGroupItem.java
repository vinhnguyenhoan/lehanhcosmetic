package com.lehanh.pama.ui.patientcase;

import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;

class GalleryCustomGroupItem extends GalleryItem {

	private GroupListener listener;
	
	public GalleryCustomGroupItem(Gallery parent, int style, GroupListener listener) {
		super(parent, style);
		this.listener = listener;
	}

	@Override
	public void _setExpanded(boolean expanded, boolean redraw) {
		if (listener != null) {
			listener.beforeExpanded(expanded, redraw, this);
		}
		super._setExpanded(expanded, redraw);
		if (listener != null) {
			listener.afterExpanded(expanded, redraw, this);
		}
	}

}
