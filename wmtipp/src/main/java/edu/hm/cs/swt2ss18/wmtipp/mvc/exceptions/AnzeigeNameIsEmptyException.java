package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class AnzeigeNameIsEmptyException extends Exception {
	
	private static final long serialVersionUID = 644185955448053732L;
	final String message;
	
	public AnzeigeNameIsEmptyException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
