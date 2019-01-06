package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class ErgebnisNichtZulaessigException extends Exception {
	
	private static final long serialVersionUID = 6947286862448600908L;
	final String message;
	
	public ErgebnisNichtZulaessigException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
