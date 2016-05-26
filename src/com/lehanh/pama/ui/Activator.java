package com.lehanh.pama.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.lehanh.pama.PamaApplication;
import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.IPatientViewPartListener;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.ui.handlers.PaitentNameStatusLine;
import com.lehanh.pama.util.PamaHome;

public class Activator implements BundleActivator, PamaApplication, IPatientViewPartListener {

	private static BundleContext context;

	private Properties appProperties;
	
	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		PamaHome.application = this;
		PamaHome.initialize();
		((IPatientManager) PamaHome.getService(IPatientManager.class)).addPaListener(this, Activator.class.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPassword(String pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProperty(String name, String defaultValues) {
		if (appProperties == null) {
			initAppProperties();
		}
		String result = appProperties.getProperty(name);
		if (defaultValues != null && result == null) {
			result = defaultValues;
		}
		return result;
	}

	private void initAppProperties() {
		if (appProperties == null) {
			appProperties = new Properties();
			try {
				URL fileURL = new URL(Platform.getInstallLocation().getURL() + "appliation.properties");
				File appPropertiesFile = null;
				appPropertiesFile = new File(fileURL.getFile());
				if (!appPropertiesFile.exists()) {
					appPropertiesFile.createNewFile();
				}
				FileInputStream fis = new FileInputStream(appPropertiesFile);
				appProperties.load(fis);
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				logError("Failed to load application.properties file", e);
			} catch (IOException e) {
				e.printStackTrace();
				logError("Failed to load application.properties file", e);
			}
		}
	}

	@Override
	public void logInfo(String message) {
		// TODO Auto-generated method stub
		System.out.println("Info:" + message);
	}

	@Override
	public void logWarning(String message, Throwable t) {
		// TODO Auto-generated method stub
		System.out.println("Warning:" + message);
		t.printStackTrace();
	}

	@Override
	public void logError(String string) {
		System.out.println("Error:" + string);
	}

	@Override
	public void logError(String string, Throwable e) {
		System.out.println("Error:" + string);
		e.printStackTrace();
	}

	@Override
	public Image loadImage(String image) {
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		// use the org.eclipse.core.runtime.Path as import
		URL url = FileLocator.find(bundle, new Path(image), null);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		return imageDescriptor.createImage();
	}

	@Override
	public void patientChanged(Patient oldPa, Patient newPa, String[] callIds) {
		if (newPa == null || StringUtils.isBlank(newPa.getName())) {
			return;
		}
		PaitentNameStatusLine.label.setText(newPa.getId() + " - " + newPa.getName());
		PaitentNameStatusLine.label.getParent().layout(true);
	}

	@Override
	public void patientCaseChanged(PatientCaseEntity oldCase, PatientCaseEntity newCase, int rootCase,
			String[] callIds) {
		// TODO Auto-generated method stub
		
	}

}
