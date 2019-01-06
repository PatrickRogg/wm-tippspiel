package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class UeberschriftZuLangException extends Exception {
	
	private static final long serialVersionUID = -5862481681099379137L;
	final String message;
	
	public UeberschriftZuLangException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
