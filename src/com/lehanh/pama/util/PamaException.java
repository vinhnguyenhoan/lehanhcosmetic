package com.lehanh.pama.util;

public class PamaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5794165903597639903L;

	public PamaException() {
	}

	public PamaException(String arg0) {
		super(arg0);
	}

	public PamaException(Throwable arg0) {
		super(arg0);
	}

	public PamaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}