package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class LoginAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 3637227428929344720L;
	final String message;
	
	public LoginAlreadyExistsException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
