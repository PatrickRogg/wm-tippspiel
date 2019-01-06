package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class TorEingabeException extends Exception {
	
	private static final long serialVersionUID = 8863395331880860309L;
	final String message;
	
	public TorEingabeException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
