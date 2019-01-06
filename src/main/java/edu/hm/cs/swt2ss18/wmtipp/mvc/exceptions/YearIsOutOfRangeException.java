package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class YearIsOutOfRangeException extends Exception {
	
	private static final long serialVersionUID = 7246052905166282857L;
	final String message;
	
	public YearIsOutOfRangeException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
