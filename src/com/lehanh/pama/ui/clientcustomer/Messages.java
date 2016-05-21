package com.lehanh.pama.ui.clientcustomer;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.lehanh.pama.ui.clientcustomer.messages"; //$NON-NLS-1$
	public static String AppointmentTable_ghichu;
	public static String AppointmentTable_hen;
	public static String AppointmentTable_thongtinkhach;
	public static String PatientTable_diachi;
	public static String PatientTable_didong;
	public static String PatientTable_gioitinh;
	public static String PatientTable_ngaysinh;
	public static String PatientTable_soid;
	public static String PatientTable_tenkhach;
	public static String TodayUserQueueView_chonngay;
	public static String TodayUserQueueView_lammoi;
	public static String TodayUserQueueView_loidb;
	public static String TodayUserQueueView_ngayhen;
	public static String UserSearchView_hoten;
	public static String UserSearchView_loidb;
	public static String UserSearchView_ngaygannhat;
	public static String UserSearchView_nhapngay;
	public static String UserSearchView_sodt;
	public static String UserSearchView_timkiem;
	public static String UserSearchView_xoaboloc;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
