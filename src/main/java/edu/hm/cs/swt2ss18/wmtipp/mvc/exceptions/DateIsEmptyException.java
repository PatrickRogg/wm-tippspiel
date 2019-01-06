package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class DateIsEmptyException extends Exception {

	private static final long serialVersionUID = 7894567925290486603L;
	final String message;
	
	public DateIsEmptyException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
