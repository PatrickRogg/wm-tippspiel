package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class PasswordIsEmptyException extends Exception {

	private static final long serialVersionUID = 4549742670235063862L;
	final String message;
	
	public PasswordIsEmptyException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
