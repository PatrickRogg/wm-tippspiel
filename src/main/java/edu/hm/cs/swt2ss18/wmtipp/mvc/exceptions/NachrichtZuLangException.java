package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class NachrichtZuLangException extends Exception{

	private static final long serialVersionUID = -4072113932702551610L;
	final String message;
	
	public NachrichtZuLangException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
