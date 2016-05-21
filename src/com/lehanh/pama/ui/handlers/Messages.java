package com.lehanh.pama.ui.handlers;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.lehanh.pama.ui.handlers.messages"; //$NON-NLS-1$
	public static String PaitentCaseStatusLine_benhan;
	public static String PaitentNameStatusLine_0;
	public static String PaitentNameStatusLine_benhan;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
