package com.lehanh.pama.ui.util;

import org.eclipse.swt.graphics.Image;

import com.lehanh.pama.PamaApplication;

public class MainApplication implements PamaApplication {
	
	@Override
	public void setPassword(String pw) {
		// do nothing
	}
	
	@Override
	public void logWarning(String message, Throwable t) {
		System.out.println("Warning " + message);
		System.out.println(t);
	}
	
	@Override
	public void logInfo(String message) {
		System.out.println("Info " + message);
	}
	
	@Override
	public void logError(String message, Throwable e) {
		System.out.println("Error " + message);
		System.out.println(e);		
	}
	
	@Override
	public void logError(String message) {
		System.out.println("Error " + message);
	}
	
	@Override
	public String getUserId() {
		// do nothing
		return null;
	}
	
	@Override
	public String getProperty(String name, String defaultValue) {
		return defaultValue;
	}
	
	@Override
	public String getPassword() {
		// do nothing
		return null;
	}

	@Override
	public Image loadImage(String string) {
		// do nothing
		return null;
	}
}