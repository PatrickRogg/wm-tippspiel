package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class SpielHatAngefangenException extends Exception {
	
	private static final long serialVersionUID = 792886447188988404L;
	final String message;
	
	public SpielHatAngefangenException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
