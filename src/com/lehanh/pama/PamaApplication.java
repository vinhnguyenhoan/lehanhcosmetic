package com.lehanh.pama;

import org.eclipse.swt.graphics.Image;

public interface PamaApplication {
	
	String getUserId();

	String getPassword();
	
	void setPassword(String pw);
	
	String getProperty(String name, String defaultValue);

	void logInfo(String message);
	
	void logWarning(String message, Throwable t);

	void logError(String string);

	void logError(String string, Throwable e);

	Image loadImage(String string);

}
