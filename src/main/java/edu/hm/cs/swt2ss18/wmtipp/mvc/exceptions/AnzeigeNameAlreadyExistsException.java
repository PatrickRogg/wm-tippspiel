package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class AnzeigeNameAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 3998509100803577547L;
	final String message;
	
	public AnzeigeNameAlreadyExistsException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
