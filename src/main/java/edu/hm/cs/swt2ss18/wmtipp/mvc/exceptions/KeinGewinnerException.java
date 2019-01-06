package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class KeinGewinnerException extends Exception {

	private static final long serialVersionUID = -76333776286365668L;
	final String message;
	
	public KeinGewinnerException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
