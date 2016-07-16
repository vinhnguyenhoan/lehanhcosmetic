package com.lehanh.pama.ui.patientcase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.patientcase.ICaseDetailHandler;
import com.lehanh.pama.patientcase.IImageInfo;
import com.lehanh.pama.patientcase.ISurgeryImageList;
import com.lehanh.pama.ui.util.PamaResourceManager;
import com.lehanh.pama.util.DateUtils;

class ImageLoader implements Runnable {

	private GalleryItem group;
	
	private int oldIndexDetail = -1;
	
	private final Gallery gallery;
	
	ImageLoader(int oldIndexDetail, Gallery gallery) {
		this.oldIndexDetail = oldIndexDetail;
		this.gallery = gallery;
	}
	
	private ICaseDetailHandler caseDetailHandler = new ICaseDetailHandler() {
		
		@Override
		public void handleCaseDetail(ISurgeryImageList imageList, int indexRoot, int indexDetail, int groupId, int caseDetailId, 
			Date examDate, int afterDays, List<Catagory> allSurCat, Map<String, Map<String, IImageInfo>> imageNamesPerSurgery) {
//			if (indexDetail != oldIndexDetail) {
//				oldIndexDetail = indexDetail;
//				group = new GalleryItem(gallery, SWT.NONE);
//				String groupName = Messages.SurgeryGallary_benhanthu + groupId + Messages.SurgeryGallary_gachngang_lankham + caseDetailId + " (" + DateUtils.convertDateDataType(examDate) + ")"; //$NON-NLS-3$ //$NON-NLS-4$
//				group.setText(groupName);
//				if (afterDays == 0) {
//					group.setText(1, "Before");//$NON-NLS-1$
//				} else {
//					group.setText(1, "After " + afterDays + " days"); //$NON-NLS-1$ //$NON-NLS-2$
//				}
//				group.setExpanded(indexRoot == 0);
//				group.setData(new GalleryItemData(groupId, caseDetailId, examDate, afterDays));
//				// TODO show group description -> date range
//			}
//			
//			if (imageNamesPerSurgery != null) {
//				for (Entry<String, Map<String, IImageInfo>> imagePerSur : imageNamesPerSurgery.entrySet()) {
//					final String surSym = imagePerSur.getKey();
//					int indexImagePerSymbol = 1;
//					for (Entry<String, IImageInfo> aImage : imagePerSur.getValue().entrySet()) {
//						String fileName = aImage.getKey();
//						Image itemImage;
//						try {
//							String folderName = folderNameFromSurgeryAndPatientId(imageList.getPatientId(), surSym);
//							itemImage = PamaResourceManager.getImage(folderName, fileName, MAX_W, MAX_H);
//							createItem(-1, group, folderName, itemImage, indexImagePerSymbol++, surSym, fileName, allSurCat, groupId, caseDetailId
//									, aImage.getValue().getDate(), examDate, afterDays);
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			createItem(-1, group, null, imageAdd, -1, null/*"Thêm ảnh"*/, null, allSurCat, groupId, caseDetailId, null, examDate, afterDays)
//					.setData(IS_ADD_ITEM, true);
		}
	};

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}