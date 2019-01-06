package edu.hm.cs.swt2ss18.wmtipp.mvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.DateIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.YearIsOutOfRangeException;

public class DateHelper {
	
	public LocalDateTime stringToDate(String s) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy | HH:mm");
		return LocalDateTime.parse(s, formatter);
	}
	
	public LocalDateTime stringToDateExcel(String s) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		return LocalDateTime.parse(s, formatter);
	}
	
	public String dateToString(LocalDateTime date) {
		String s;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy | HH:mm");
		
		s = date.format(formatter);
		return s; 
	}
	
	public String dateToStringDate(LocalDateTime date) {
		String s;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy");
		
		s = date.format(formatter);
		return s; 
	}
	
	public String dateToStringTime(LocalDateTime date) {
		String s;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		
		s = date.format(formatter);
		return s; 
	}
	
	public String dateToStringHtml(LocalDateTime date) {
		String s;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		
		s = date.format(formatter);
		return s; 
	}

	public String dateToStringJavascript(LocalDateTime date) {
		String s;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd, yyyy HH:mm:ss");
		
		s = date.format(formatter);
		return s; 
	}
	
	public LocalDateTime htmlStringToDate(String s) throws DateIsEmptyException, YearIsOutOfRangeException {
		if(s.length() > 15) {
			throw new YearIsOutOfRangeException("Bitte überprüfen sie die Eingabe des Jahres!");
		}
		if(s.length() == 0) {
			throw new DateIsEmptyException("Bitte geben sie ein Datum ein!");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		return LocalDateTime.parse(s, formatter);
	}
}
