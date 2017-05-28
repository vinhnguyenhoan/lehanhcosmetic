package com.lehanh.pama.ui.patientcase;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;

import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.ui.util.PamaResourceManager;

class GalleryCustomGroupItem extends GalleryItem {

	private boolean expanded;
	private boolean loadedItems = false;
	
	private final int maxW;
	private final int maxH;
	private final Image imageAdd;
	
	public GalleryCustomGroupItem(Gallery parent, int style, String groupName, int maxW, int maxH, Image imageAdd) {
		super(parent, style);
		setText(groupName);
		this.expanded = isExpanded();
		this.maxW = maxW;
		this.maxH = maxH;
		this.imageAdd = imageAdd;
	}

	@Override
	public void _setExpanded(boolean expanded, boolean redraw) {
		final boolean expandchanged = this.expanded != expanded;
		this.expanded = expanded;
		if (expandchanged) {
			this.beforeExpanded();
		}
		super._setExpanded(expanded, redraw);
		if (expandchanged) {
			this.afterExpanded();
		}
	}
	private void beforeExpanded() {
		// TODO Auto-generated method stub
	}
	private void afterExpanded() {
		if (!loadedItems) {
			loadItems();
			loadedItems = true;
		}
	}

	@SuppressWarnings("unchecked")
	private void loadItems() {
		itemParametersList.stream().forEach(p -> {
			int index = 0;
			createGallaryItem((Boolean) p[index++], (Integer) p[index++], (String) p[index++], (Integer) p[index++], (String) p[index++], (String) p[index++], 
					(List<Catagory>) p[index++],(Integer) p[index++], (Integer) p[index++], (String) p[index++], (Date) p[index++], (Integer) p[index++]);
		});
		getParent().redraw();
		itemParametersList.clear();
	}

	private final ConcurrentLinkedQueue<Object[]> itemParametersList = new ConcurrentLinkedQueue<>();
			
	void createAddNewButtonGallaryItem(int orderIndex, String folder, int indexImagePerSymbol, String surSym, String imageName, 
			List<Catagory> allSurCat, int groupId, int caseDetailId, String date, Date examDate, int afterDays) {
		this.createGallaryItem(true, orderIndex, folder, indexImagePerSymbol, surSym, imageName, 
				allSurCat, groupId, caseDetailId, date, examDate, afterDays);
	}
	void createGallaryItem(int orderIndex, String folder, int indexImagePerSymbol, String surSym, String imageName, 
			List<Catagory> allSurCat, int groupId, int caseDetailId, String date, Date examDate, int afterDays) {
		this.createGallaryItem(false, orderIndex, folder, indexImagePerSymbol, surSym, imageName, 
				allSurCat, groupId, caseDetailId, date, examDate, afterDays);
	}
	private void createGallaryItem(boolean isNewItemButton, int orderIndex, String folder, int indexImagePerSymbol, String surSym, String imageName, 
			List<Catagory> allSurCat, int groupId, int caseDetailId, String date, Date examDate, int afterDays) {
		if (!expanded) {
			Object[] params = new Object[]{isNewItemButton, orderIndex, folder, indexImagePerSymbol, surSym, imageName, 
					allSurCat, groupId, caseDetailId, date, examDate, afterDays};
			itemParametersList.add(params);
			return;
		}
		
		GalleryItem item = null;
		if (orderIndex < 0) {
			item = new GalleryItem(this, SWT.NONE);
		} else {
			item = new GalleryItem(this, SWT.NONE, orderIndex);
		}
		
		Image itemImage = imageAdd;
		if (!isNewItemButton) {
			try {
				itemImage = PamaResourceManager.getImage(folder, imageName, maxW, maxH);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (itemImage != null) {
			item.setImage(itemImage);
		}
		if (surSym != null) {
			item.setText(0, surSym);
			item.setText(1, "STT: " + indexImagePerSymbol); //$NON-NLS-1$
		}
		item.setData(new GalleryItemData(folder, surSym, date, imageName, groupId, caseDetailId, examDate, afterDays, allSurCat));
		if (isNewItemButton) {
			item.setData(SurgeryGallary.IS_ADD_ITEM, true);
		}
		// item.setData(FOLDER_PATH, folder);
		// item.setData(SUR_SYM, surSym);
		// item.setData(DATE, date);
		// item.setData(IMAGE_NAME, imageName);
		// item.setData(GROUP_ID, groupId);
		// item.setData(DETAIL_ID, caseDetailId);
		// item.setData(ALL_SURGERY, allSurCat);
		item.addDisposeListener(itemDispose);
	}
	private static DisposeListener itemDispose = new DisposeListener() {
		
		@Override
		public void widgetDisposed(DisposeEvent e) {
			((GalleryItem) e.widget).getImage().dispose();
		}
	};

}
