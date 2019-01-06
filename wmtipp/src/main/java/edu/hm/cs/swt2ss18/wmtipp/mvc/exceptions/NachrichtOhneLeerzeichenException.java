package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class NachrichtOhneLeerzeichenException extends Exception {

	private static final long serialVersionUID = -4702564678992833717L;
	final String message;
	
	public NachrichtOhneLeerzeichenException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
