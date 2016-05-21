package com.lehanh.pama.ui.util;

public class InvalidInputFormException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6687347921159815080L;

	public InvalidInputFormException() {
        super();
    }

    public InvalidInputFormException(String message) {
        super(message);
    }

    public InvalidInputFormException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputFormException(Throwable cause) {
        super(cause);
    }

}
