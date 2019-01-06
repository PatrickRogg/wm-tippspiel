package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class KeinUnentschiedenBeiVerlaengerungException extends Exception {

	private static final long serialVersionUID = 7883247977616780605L;
	final String message;
	
	public KeinUnentschiedenBeiVerlaengerungException(String s) {
		super();
		message = s;
	}
	
	public String getMessage() {
		return this.message;
	}
}
