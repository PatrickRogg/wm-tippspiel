package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class MannschaftsNameIsEmptyException extends Exception {

	private static final long serialVersionUID = 7348036077208123231L;
	final String message;
	
	public MannschaftsNameIsEmptyException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
