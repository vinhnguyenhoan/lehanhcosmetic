package com.lehanh.pama.patientcase;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.lehanh.pama.catagory.AppointmentCatagory;
import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.DiagnoseCatagory;
import com.lehanh.pama.catagory.DrCatagory;
import com.lehanh.pama.catagory.PrognosticCatagory;
import com.lehanh.pama.catagory.ServiceCatagory;
import com.lehanh.pama.catagory.SurgeryCatagory;
import com.lehanh.pama.util.DateUtils;
import com.lehanh.pama.util.PamaException;
import com.lehanh.pama.util.Utils;

public class PatientCaseList implements IPatientCaseList {

	private final List<PatientCaseEntity> patientCases;

	private final SurgeryImageList surgeryImageList;
	
	private String summary;
	
	private final boolean isListRootCase;

	// Manage sub patient case list
	private PatientCaseEntity creatingDetailExamCase; // create instance for creating data so when clear data this info will delete
	private final Map<Integer, PatientCaseList> detailPAList;

	private final Long patientId;
	
	private static final Comparator<? super PatientCaseEntity> idDescComparator = new Comparator<PatientCaseEntity>() {

		@Override
		public int compare(PatientCaseEntity p1, PatientCaseEntity p2) {
			Integer id1 = p1.getId();
			Integer id2 = p2.getId();
			return id2.compareTo(id1);
		}
	};
	
	PatientCaseList(Long patientId, List<PatientCaseEntity> patientCases, String summary) {
		this(patientId, patientCases, summary, true);
		/*if (patientCases.isEmpty()) {
			createRootCase();
		}*/
	}
	
	private PatientCaseList(Long patientId, List<PatientCaseEntity> patientCases, String summary, boolean isRoot) {
		this.patientId = patientId;
		this.isListRootCase = isRoot;
		this.patientCases = patientCases;
		this.setSummary(summary);
		// order list
		Collections.sort(this.patientCases, idDescComparator);
		detailPAList = new TreeMap<Integer, PatientCaseList>();
		for (PatientCaseEntity pCase : this.patientCases) {
			detailPAList.put(pCase.getId(), new PatientCaseList(this.patientId, pCase.getReExamInfo(), null, false));
		}
		
		if (isRoot) {
			this.surgeryImageList = new SurgeryImageList(this.patientId, this.patientCases);
		} else {
			surgeryImageList = null;
		}
	}

	@Override
	public boolean isListRootCase() {
		return this.isListRootCase;
	}
	
	@Override
	public IPatientCaseList createRootCase() {
		if (!isListRootCase) {
			throw new PamaException(Messages.PatientCaseList_Cannotcreaterootcasefromthislist);
		}
		if (!detailPAList.isEmpty() && ((PatientCaseList) detailPAList.values().toArray()[0]).patientCases.isEmpty()) {
			throw new InvalidParameterException(Messages.PatientCaseList_benhangannhatdangtrong);
		}
		for (PatientCaseList subList : this.detailPAList.values()) {
			if (subList.creatingDetailExamCase != null) {
				throw new PamaException(Messages.PatientCaseList_cannotcreatenewrootcasewhilecretingdetailexam);
			}
		}
		
		PatientCaseEntity newRootCase = new PatientCaseEntity(DateUtils.convertDateDataType(GregorianCalendar.getInstance().getTime()));
		this.patientCases.add(0, newRootCase);
		// set id after add to list
		newRootCase.setId(patientCases.size());
		
		PatientCaseList result = new PatientCaseList(this.patientId, newRootCase.getReExamInfo(), null, false);
		this.detailPAList.put(newRootCase.getId(), result);
		return result;
	}
	
	@Override
	public PatientCaseEntity createDetailCase(PatientCaseStatus status) {
		if (isListRootCase) {
			throw new PamaException(Messages.PatientCaseList_cannotcreatenewdetailexamwithoutaparentcase);
		}
		if (status == null) {
			throw new PamaException(Messages.PatientCaseList_cannotcreatenewdetailexamwithoutstatus);
		}
		if (creatingDetailExamCase != null) {
			throw new PamaException(Messages.PatientCaseList_cannotcreatenewexamwhileeditingandnotsubmityet);
		}
		
		// create new instance and update + add to list when call update method
		this.creatingDetailExamCase = new PatientCaseEntity(DateUtils.convertDateDataType(GregorianCalendar.getInstance().getTime()), status);

		// set default value from previous version for re exam
		if (PatientCaseStatus.EXAM == status) {
			for (PatientCaseEntity pe : patientCases) {
				if (PatientCaseStatus.EXAM == pe.getStatusEnum() && pe.getSurgeryCatagoryNames() != null && !pe.getSurgeryCatagoryNames().isEmpty()) {
					PatientCaseEntity previousVersion = getLastExamHaveStatus(PatientCaseStatus.EXAM);
					creatingDetailExamCase.setServiceNames(previousVersion.getServiceNames());
					creatingDetailExamCase.setPrognosticCatagoryNames(previousVersion.getPrognosticCatagoryNames());
					creatingDetailExamCase.setDiagnoseCatagoryNames(previousVersion.getDiagnoseCatagoryNames());
					creatingDetailExamCase.setSurgeryCatagoryNames(previousVersion.getSurgeryCatagoryNames() );
				}
			}
		}
		
		return creatingDetailExamCase;
	}
	
	@Override
	public void updateCase(PatientCaseEntity paCaseEntity, DrCatagory drCat,
			List<ServiceCatagory> serviceList,
			List<PrognosticCatagory> progCatList, String prognosticOtherText,
			List<DiagnoseCatagory> diagCatList, String diagOtherText,
			String noteFromPa, String noteFromDr,
			List<SurgeryCatagory> surList, String surgeryNote,
			Date surgeryDate, boolean complication, boolean beauty,
			String smallSurgery, String drAdvice, Date nextApp,
			AppointmentCatagory appPurpose, String appNote, String paInfo) {
		
		// validate input data
		if (isListRootCase) {
			throw new PamaException(Messages.PatientCaseList_cannotupdatenewdetailexamwithoutaparentcase);
		}
		// validate date before update
		if (PatientCaseStatus.EXAM == paCaseEntity.getStatusEnum()) {
			if (surList == null || surList.isEmpty()) {
				PatientCaseEntity lastExam = getLastExamHaveStatus(PatientCaseStatus.EXAM);
				if (lastExam == null) {
					throw new InvalidParameterException(Messages.PatientCaseList_phaichonphauthuat);
				}
			}
		}
		PatientCaseEntity sampleEmtity = new PatientCaseEntity(paCaseEntity.getDateAsText(), paCaseEntity.getStatusEnum());
		sampleEmtity.updateData(drCat == null ? null : drCat.getId(), Catagory.getListName(serviceList), Catagory.getListName(progCatList), prognosticOtherText,
				Catagory.getListName(diagCatList), diagOtherText, noteFromPa, noteFromDr, Catagory.getListName(surList), surgeryNote,
				surgeryDate, complication, beauty, smallSurgery, drAdvice, nextApp,
				appPurpose, appNote, paInfo);
		
		// validate ok then update data
		if (creatingDetailExamCase != null && creatingDetailExamCase == paCaseEntity) {
			patientCases.add(0, paCaseEntity);
			// set id after add to list
			paCaseEntity.setId(patientCases.size());
			
		}
		paCaseEntity.updateData(drCat == null ? null : drCat.getId(), Catagory.getListName(serviceList), Catagory.getListName(progCatList), prognosticOtherText,
				Catagory.getListName(diagCatList), diagOtherText, noteFromPa, noteFromDr, Catagory.getListName(surList), surgeryNote,
				surgeryDate, complication, beauty, smallSurgery, drAdvice, nextApp,
				appPurpose, appNote, paInfo);
	}
	
	@Override
	public boolean isLastCreatedExam(PatientCaseEntity selectedEntity) {
		if (patientCases == null || patientCases.isEmpty()) {
			return false;
		}
		return patientCases.get(0) == selectedEntity;
	}
	
	@Override
	public boolean isCreatingExam(PatientCaseEntity entity) {
		return this.creatingDetailExamCase != null && this.creatingDetailExamCase == entity;
	}

	@Override
	public Object[] getAllVersions() {
		Object[] allVers = patientCases.toArray();
    	if (creatingDetailExamCase != null) {
    		LinkedList<Object> allVersList = new LinkedList<Object>(Arrays.asList(allVers));
    		allVersList.addFirst(creatingDetailExamCase);
    		allVers = allVersList.toArray();
    	}
    	return allVers;
	}
	
	@Override
	public IPatientCaseList getSubList(int parentId) {
		return detailPAList.get(parentId);
	}
	
	@Override
	public PatientCaseEntity getLastExamHaveStatus(PatientCaseStatus... caseStatus) {
		if (caseStatus == null || caseStatus.length == 0) {
			if (this.creatingDetailExamCase != null) {
				return this.creatingDetailExamCase;
			}
			return patientCases.isEmpty() ? null : patientCases.get(0);
		}
		
		if (patientCases == null || patientCases.isEmpty()) {
			return null;
		}
		
		for (PatientCaseEntity pe : patientCases) {
			for (PatientCaseStatus cs : caseStatus) {
				if (cs == pe.getStatusEnum()) {
					return pe;
				}
			}
		}
		return null;
	}
	
	@Override
	public boolean isNotHaveAnyEntity() {
		return this.creatingDetailExamCase == null && (this.patientCases == null || this.patientCases.isEmpty());
	}

	/**
	 * Summary manage
	 */
	@Override
	public String getSummary(boolean includeConsult) {
		String result = summary;
		if (StringUtils.isBlank(result)) {
			result = generateSummary(this.patientCases, includeConsult);
		}
		return result;
	}

	/**
	 * 1) Lần khám (xx/xx/xxxx):
	 * 
	 * 1.1) Lần tái khám (xx/xx/xxxx): 
	 * 
	 * 1.2) Lần tái khám (xx/xx/xxxx): 
	 * 
	 * 2) Tư vấn (xx/xx/xxxx)
	 * 
	 * 3) Lần khám (xx/xx/xxxx)
	 * 
	 * 3.1) Lần tái khám (xx/xx/xxxx)
	 *  
	 * @param patientCases
	 * @param includeConsult 
	 * @return
	 */
	private static final String generateSummary(List<PatientCaseEntity> patientCases, boolean includeConsult) {
		if (patientCases == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		
		String dateEvent;
		int index = 0, indexReExam = 0;
		for (PatientCaseEntity pE : patientCases) {
			dateEvent = pE.getDateAsText();

			++index;
			//result.append("Bệnh án ").append(String.valueOf(index));
			result.append(Utils.intToRomanNumeral(index)).append(Messages.PatientCaseList_chambenhan_mongoac).append(dateEvent).append(Messages.PatientCaseList_9)
				  .append(System.lineSeparator());
			
			if (pE.getReExamInfo() != null) {
				indexReExam = 0;
				for (PatientCaseEntity reE : pE.getReExamInfo()) {
					++indexReExam;
					
					if (PatientCaseStatus.CONSULT == pE.getStatusEnum()) {
						if (!includeConsult) {
							continue;
						}
						result.append(String.valueOf(indexReExam)).append(Messages.PatientCaseList_chamtuvandongngoac).append(dateEvent).append(Messages.PatientCaseList_11)
                              .append(System.lineSeparator())
                              .append(generateSummary(reE));
					} else if (PatientCaseStatus.EXAM == pE.getStatusEnum()) {
						result.append(String.valueOf(indexReExam)).append(Messages.PatientCaseList_thamkhammongoac).append(reE.getDateAsText()).append(Messages.PatientCaseList_13)
                              .append(System.lineSeparator())
						      .append(generateSummary(reE));
					}
				}
			}
		}
		
		return result.toString();
	}

	private static String generateSummary(PatientCaseEntity detailE) {
		// TODO Auto-generated method stub
		return StringUtils.EMPTY;
	}

	String setSummary(String summary) {
		this.summary = summary;
		return this.summary;
	}

	@Override
	public String getSurgerySummary() {
		final String postText = Messages.PatientCaseList_14;
		StringBuilder result = new StringBuilder();
		TreeSet<String> surgeryCurr = new TreeSet<String>();
		for (PatientCaseEntity entity : this.patientCases) {
			if (entity.getSurgeryCatagoryNames() == null) {
				continue;
			}
			
			for (String surgery : entity.getSurgeryCatagoryNames()) {
				if (surgery == null) {
					System.out.println();
					continue;
				}
				if (surgeryCurr.contains(surgery)) {
					continue;
				}
				surgeryCurr.add(surgery);
				result.append(surgery).append(postText);
			}
		}
		
		if (result.length() > postText.length()) {
			return result.substring(0, result.length() - postText.length());
		}
		
		return result.toString();
	}
	
	/**
	 * Manage images
	 */
	@Deprecated
	@Override
	public ISurgeryImageList getSurgeryImageList() {
		return this.surgeryImageList;
	}
	
	@Override
	public IPrescription getPharmacyList(int rootId, int detalId) {
		if (isListRootCase) {
			throw new PamaException(Messages.PatientCaseList_phaicoidcuabenhan);
		}
		PatientCaseList details = this.detailPAList.get(rootId);
		if (details == null) {
			return null;
		}
		for (PatientCaseEntity caseEntity : details.patientCases) {
			if (detalId == caseEntity.getId()) {
				return new Prescription(patientId, caseEntity);
			}
		}
		return null;
	}

	@Override
	public int calculateDateAfterRoot(int rootId, int id) throws ParseException {
		PatientCaseList detailList = detailPAList.get(rootId);
		if (detailList == null) {
			return 0;
		}
		
		Date beforeDate = null;
		for (int i = detailList.patientCases.size() - 1; i > -1; i--) {
			PatientCaseEntity pC = detailList.patientCases.get(i);
			if (PatientCaseStatus.EXAM == pC.getStatusEnum() && pC.getSurgeryCatagoryNames() != null 
					&& !pC.getSurgeryCatagoryNames().isEmpty()) {
				beforeDate = pC.getDate();
				break;
			}
		}
		
		if (beforeDate == null) {
			return 0;
		}
		
		for (PatientCaseEntity paE : detailList.patientCases) {
			if (paE.getId() == id) {
				return DateUtils.calculateDate(paE.getDate(), beforeDate);
			}
		}
		
		return 0;
	}

}