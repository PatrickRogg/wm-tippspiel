package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class LoginIsAdminException extends Exception {

	private static final long serialVersionUID = 7186638543882156140L;
	final String message;
	
	public LoginIsAdminException(String s) {
		super();
		message = s;
	}
	
	public String getMessage() {
		return this.message;
	}
}
