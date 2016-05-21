package com.lehanh.pama.patientcase;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.lehanh.pama.patientcase.messages"; //$NON-NLS-1$
	public static String AppointmentSchedule_phaichonngayhen;
	public static String Patient_nam;
	public static String Patient_nu;
	public static String Patient_phainhapngaysinh;
	public static String Patient_phainhapsodienthoai;
	public static String Patient_phainhapten;
	public static String PatientCaseEntity_phaichonbacsy;
	public static String PatientCaseEntity_phaichonchandoan;
	public static String PatientCaseEntity_phaichondichvu;
	public static String PatientCaseEntity_phaichontrieuchung;
	public static String PatientCaseList_11;
	public static String PatientCaseList_13;
	public static String PatientCaseList_14;
	public static String PatientCaseList_9;
	public static String PatientCaseList_benhangannhatdangtrong;
	public static String PatientCaseList_cannotcreatenewdetailexamwithoutaparentcase;
	public static String PatientCaseList_cannotcreatenewdetailexamwithoutstatus;
	public static String PatientCaseList_cannotcreatenewexamwhileeditingandnotsubmityet;
	public static String PatientCaseList_cannotcreatenewrootcasewhilecretingdetailexam;
	public static String PatientCaseList_cannotupdatenewdetailexamwithoutaparentcase;
	public static String PatientCaseList_chambenhan_mongoac;
	public static String PatientCaseList_chamtuvandongngoac;
	public static String PatientCaseList_Cannotcreaterootcasefromthislist;
	public static String PatientCaseList_phaichonphauthuat;
	public static String PatientCaseList_phaicoidcuabenhan;
	public static String PatientCaseList_thamkhammongoac;
	public static String PatientCaseStatus_thamkham;
	public static String PatientCaseStatus_tuvan;
	public static String PatientManager_cannotfindappointmenttype;
	public static String PatientManager_chuachonbenhan;
	public static String PatientManager_chuachonbenhnhan;
	public static String PatientManager_loicapnhapdb;
	public static String PatientManager_loicapnhatdb;
	public static String SurgeryImageList_chonbenhanvalankhamcuthe;
	public static String SurgeryImageList_khongthayphauthuat;
	public static String SurgeryImageList_tenfileanhkodung;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
