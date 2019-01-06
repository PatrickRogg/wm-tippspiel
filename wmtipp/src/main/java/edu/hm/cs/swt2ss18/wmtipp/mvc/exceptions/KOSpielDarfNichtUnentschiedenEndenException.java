package edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions;

public class KOSpielDarfNichtUnentschiedenEndenException extends Exception {
	
	private static final long serialVersionUID = -5077399946627535331L;
	String message;
	
	public KOSpielDarfNichtUnentschiedenEndenException(String s) {
		super();
		message = s;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
