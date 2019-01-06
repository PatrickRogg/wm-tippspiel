package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class LoginIsEmptyException extends Exception {

	private static final long serialVersionUID = -1116151006832666724L;
	final String message;
	
	public LoginIsEmptyException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
