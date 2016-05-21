package com.lehanh.pama.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.lehanh.pama.ICatagoryManager;
import com.lehanh.pama.IService;
import com.lehanh.pama.PamaApplication;
import com.lehanh.pama.catagory.CatagoryManager;
import com.lehanh.pama.db.DatabaseManager;
import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.PatientManager;

public class PamaHome {

	public static PamaApplication application;

	public static final String DB_NAME_PRO_NAME = "lh_pama_db_name";
	public static final String DB_USER_NAME_PRO_NAME = "lh_pama_user_name";
	public static final String DB_PASS_PRO_NAME = "lh_pama_pass";
	public static final String DB_IP_PRO_NAME = "lh_pama_db_ip";
	
	public static final String SURGERY_IMAGE_PATH = "lh_pama_surgery_image_path_server";
	public static final String DEFAULT_SURGERY_IMAGE_PATH = "D:\\Pama\\SharedImage\\Server\\PhauThuat";
	
	public static final String PATIENT_IMAGE_PATH = "lh_patient_image_path";
	public static final String DEFAULT_PATIENT_IMAGE_PATH = "D:\\Pama\\patientpics";
	
	public static String getDbName(String defaultDB) {
		return application.getProperty(DB_NAME_PRO_NAME, defaultDB);
	}

	public static String getDbUserName(String defaultUserName) {
		return application.getProperty(DB_USER_NAME_PRO_NAME, defaultUserName);
	}

	public static String getDbPass(String defaultPass) {
		return application.getProperty(DB_PASS_PRO_NAME, defaultPass);
	}

	public static String getDbIp(String defaultIP) {
		return application.getProperty(DB_IP_PRO_NAME, defaultIP);
	}
	
	private static final Map<Class<?>, IService> serviceManager = new HashMap<Class<?>, IService>();
	private static final List<IService> serviceManagerOrderedList = new LinkedList<IService>();

	static {
		putService(ICatagoryManager.class, new CatagoryManager());
		putService(IPatientManager.class, new PatientManager());
	}
	
	private static final void putService(Class<?> type, IService instance) {
		serviceManager.put(type, instance);
		serviceManagerOrderedList.add(instance);
	}
	
	public static final IService getService(Class<?> service) {
		return serviceManager.get(service);
	}

	public static final void initialize() {
		try {
			DatabaseManager.initialize();
			for (IService service : serviceManagerOrderedList) {
				service.initialize();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}