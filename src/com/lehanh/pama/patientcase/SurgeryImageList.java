package com.lehanh.pama.patientcase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.lehanh.pama.ICatagoryManager;
import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.util.DateUtils;
import com.lehanh.pama.util.PamaException;
import com.lehanh.pama.util.PamaHome;

class SurgeryImageList implements ISurgeryImageList {

	private final Map<Integer, PatientCaseEntity> patientCases;

	private final Long patientId;
	
	private static final String surgeryPath;
	static {
		surgeryPath = PamaHome.application.getProperty(PamaHome.SURGERY_IMAGE_PATH, PamaHome.DEFAULT_SURGERY_IMAGE_PATH);
	}
	
	private static final String U_DATE = "u_date"; //$NON-NLS-1$
	private static final String ID = "id"; //$NON-NLS-1$
	
	private static final String IMAGE_SURGERY_COUNT = "count"; //$NON-NLS-1$
	private static final String IMAGE_SURGERY_STATICTIS = "statictis"; //$NON-NLS-1$

	private class ImageInfo implements IImageInfo {

		private TreeMap<String, Object> data;
		private String imgName;
		private String surgeryName;
		
		private ImageInfo(String surgeryName, String imgName, TreeMap<String, Object> data) {
			this.surgeryName = surgeryName;
			this.imgName = imgName;
			this.data = data;
		}
		
		/**
		 * Create when add new image
		 */
		private ImageInfo(String surgeryName, int index, String convertDateDataType) {
			this.surgeryName = surgeryName;
			data = new TreeMap<String, Object>();
			data.put(U_DATE, convertDateDataType);
			data.put(ID, index);
		}

		@Override
		public String getImageName() {
			return imgName;
		}
		
		@Override
		public String getDate() {
			return getValue(U_DATE, data, (String) null);
		}
		
		@Override
		public Number getId() {
			return getValue(ID, data, -1);
		}

		@Override
		public String getFolderName() {
			return folderNameFromSurgeryAndPatientId(getPatientId(), surgeryName);
		}
		
	}
	
	public static final String[] getDataFromImageName(String imageName) {
		if (StringUtils.isBlank(imageName)) {
			return null;
		}
		
		String[] splited = imageName.split("-"); //$NON-NLS-1$
		if (splited == null || splited.length != 5) {
			return null;
		}
		return splited;
	}
	
	private ICatagoryManager catM;

	SurgeryImageList(Long patientId, List<PatientCaseEntity> patientCases) {
		this.patientId = patientId;
		this.patientCases = new TreeMap<Integer, PatientCaseEntity>();
		//Collections.sort(patientCases, idAscComparator);
		for (PatientCaseEntity entity : patientCases) {
			this.patientCases.put(entity.getId(), entity);
		}
		this.catM = (ICatagoryManager) PamaHome.getService(ICatagoryManager.class);
	}
	
	@Override
	public Long getPatientId() {
		return this.patientId;
	}

	@SuppressWarnings("unchecked")
	private static final <T> T getValue(String paramName, Map<String, Object> data, T defaultV) {
		if (data != null && data.containsKey(paramName)) {
			return (T) data.get(paramName);
		}
		return defaultV;
	}

	@Override
	public void iteratorCaseDetail(ICaseDetailHandler caseDetailHandler) throws ParseException {
		int indexRoot = 0; int indexDetail = 0;
		for (Entry<Integer, PatientCaseEntity> entry : patientCases.entrySet()) {
			List<PatientCaseEntity> detailList = entry.getValue().getReExamInfo();
			if (detailList == null) {
				continue;
			}
			final Date beforeDate = getBeforeDate(detailList);
			indexDetail = 0;
			for (PatientCaseEntity detail : detailList) {
				List<String> allSurgery = detail.getSurgeryCatagoryNames();
				if (allSurgery == null || allSurgery.isEmpty()) {
					continue;
				}
				List<Catagory> allSurCat = catM.getCatagoryByTypeAndName(CatagoryType.SURGERY, allSurgery);
				
				TreeMap<String, TreeMap<String, TreeMap<String, Object>>> allPics = detail.getPicInfos();
				// TODO sort other by surgery sym and image id
				Map<String, Map<String, IImageInfo>> allPicInfos = null;
				if (allPics != null && !allPics.isEmpty()) {
					allPicInfos = new TreeMap<String, Map<String, IImageInfo>>();
					Map<String, IImageInfo> picInfos;
					for (Entry<String, TreeMap<String, TreeMap<String, Object>>> entryPicOfSurgery : allPics.entrySet()) {
						picInfos = new TreeMap<String, IImageInfo>();
						for (Entry<String, TreeMap<String, Object>> entryPic : entryPicOfSurgery.getValue().entrySet()) {
							if (IMAGE_SURGERY_STATICTIS.equals(entryPic.getKey())) {
								continue;
							}
							ImageInfo iII = new ImageInfo(entryPicOfSurgery.getKey(), entryPic.getKey(), entryPic.getValue());
							picInfos.put(entryPic.getKey(), iII);
						}
						allPicInfos.put(entryPicOfSurgery.getKey(), picInfos);
					}
				}
				caseDetailHandler.caseDetailLoaded(this, indexRoot, indexDetail, entry.getKey(), detail.getId(), detail.getDate()
						, DateUtils.calculateDate(detail.getDate(), beforeDate), allSurCat, allPicInfos);
				indexDetail++;
			}
			
			indexRoot++;
		}
	}

	private static final Date getBeforeDate(List<PatientCaseEntity> detailList) throws ParseException {
		for (int i = detailList.size() - 1; i > -1; i--) {
			PatientCaseEntity pC = detailList.get(i);
			if (PatientCaseStatus.EXAM == pC.getStatusEnum() && pC.getSurgeryCatagoryNames() != null 
					&& !pC.getSurgeryCatagoryNames().isEmpty()) {
				return pC.getDate();
			}
		}
		return null;
	}

	@Override
	public void deletedImages(List<String> imageNameToDelete) {
		if (imageNameToDelete == null) {
			return;
		}
		for (String imageName : imageNameToDelete) {
			String[] imageInfos = getDataFromImageName(imageName);
			deleteImage(imageName, imageInfos);
		}
	}

	private void deleteImage(String imageName, String[] imageInfos) {
		if (imageInfos == null) {
			throw new PamaException(Messages.SurgeryImageList_tenfileanhkodung);
		}
		Integer rootId = Integer.valueOf(((String) imageInfos[1]));
		PatientCaseEntity allDetail = this.patientCases.get(rootId);
		if (allDetail == null) {
			return;
		}
		
		List<PatientCaseEntity> examList = allDetail.getReExamInfo();
		if (examList == null) {
			return;
		}
		Integer detailId = Integer.valueOf(((String) imageInfos[2]));
		if (detailId == null) {
			return;
		}
		PatientCaseEntity entityToDelete = null;
		for (PatientCaseEntity entity : examList) {
			if (entity.getId() == detailId) {
				entityToDelete = entity;
				break;
			}
		}
		if (entityToDelete == null || entityToDelete.getPicInfos() == null) {
			return;
		}
		
		String surSym = (String) imageInfos[3];
		TreeMap<String, TreeMap<String, Object>> allImagePerSur = entityToDelete.getPicInfos().get(surSym);
		if (allImagePerSur == null) {
			return;
		}
		allImagePerSur.remove(imageName);
	}

	@Override
	public IImageInfo addImage(final String filePath, final int groupId, final int detailId, final String surgeryName, final String extension) {
		PatientCaseEntity root = patientCases.get(groupId);
		if (root == null) {
			throw new IllegalArgumentException(Messages.SurgeryImageList_chonbenhanvalankhamcuthe);
		}

		Catagory surgeryCat = catM.getCatagoryByTypeAndName(CatagoryType.SURGERY, surgeryName);
		if (surgeryCat == null) {
			throw new IllegalArgumentException(Messages.SurgeryImageList_khongthayphauthuat + surgeryName);
		}
		final String surgerySymbol = surgeryCat.getSymbol();
		
		String correctedExt = extension;
		if (!extension.startsWith(".")) { //$NON-NLS-1$
			correctedExt = "." + extension; //$NON-NLS-1$
		}
		
		List<PatientCaseEntity> details = root.getReExamInfo();
		for (PatientCaseEntity detail : details) {
			if (detail.getId() == detailId) {
				TreeMap<String, TreeMap<String, TreeMap<String, Object>>> picInfos = detail.getPicInfos();
				if (picInfos == null) {
					picInfos = new TreeMap<String, TreeMap<String,TreeMap<String,Object>>>();
					detail.setPicInfos(picInfos);
				}
				
				TreeMap<String, TreeMap<String, Object>> imageBySur = picInfos.get(surgerySymbol);
				if (imageBySur == null) {
					imageBySur = new TreeMap<String, TreeMap<String,Object>>();
					picInfos.put(surgerySymbol, imageBySur);
				}
				
				TreeMap<String, Object> imageSta = imageBySur.get(IMAGE_SURGERY_STATICTIS);
				if (imageSta == null) {
					imageSta = new TreeMap<String, Object>();
					imageSta.put(IMAGE_SURGERY_COUNT, (int) 0);
					imageBySur.put(IMAGE_SURGERY_STATICTIS, imageSta);
				}
				int imageCount = ((Number) imageSta.get(IMAGE_SURGERY_COUNT)).intValue() + (int) 1;
				imageSta.put(IMAGE_SURGERY_COUNT, imageCount);
				
				// store image date that saved
				ImageInfo iI = new ImageInfo(surgerySymbol, imageCount, DateUtils.convertDateDataType(GregorianCalendar.getInstance()));
				String imageName = generateImageName(patientId, groupId, detailId, surgerySymbol, iI.getId()) + correctedExt;
				imageBySur.put(imageName, iI.data);
				iI.imgName = imageName;
				
				moveImageFile(filePath, surgerySymbol, iI.getImageName());
				return iI;//imageName;
			}
		}
		
		return null;
	}

	private static final String generateImageName(long patientId, int rootId, int detailId, String surgerySymbol, Number id) {
		return String.valueOf(patientId) + "-" + String.valueOf(rootId) + "-" + String.valueOf(detailId) + "-" + surgerySymbol + "-" + id.toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	private void moveImageFile(String filePath, String surgerySymbol, String imageName) {
		final long patientId = this.getPatientId();
		String toFolder = folderNameFromSurgeryAndPatientId(patientId, surgerySymbol);
		try {
			File toFolderFile = new File(toFolder);
			if (!toFolderFile.exists()) {
				if (!toFolderFile.mkdirs()) {
					throw new PamaException("Can not create folder " + toFolder); //$NON-NLS-1$
				}
			}
			Files.copy(new File(filePath).toPath(), new File(toFolder + imageName).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new PamaException(e);
		}
	}

	private static final String folderNameFromSurgeryAndPatientId(long pId, String surgerySymbol) {
		String pIdPrefix = String.valueOf(pId);
		if (pIdPrefix.length() > 1) {
			pIdPrefix = pIdPrefix.substring(0, pIdPrefix.length() - 1);
		} else {
			pIdPrefix = StringUtils.EMPTY;
		}
		String result = pIdPrefix + "0-" + pIdPrefix + "9"; // example 16945 -> 16940-16949 //$NON-NLS-1$ //$NON-NLS-2$
		return SurgeryImageList.surgeryPath + "/" + surgerySymbol + "/" + result + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}