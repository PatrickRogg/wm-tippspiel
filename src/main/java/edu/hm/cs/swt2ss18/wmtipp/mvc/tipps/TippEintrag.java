package edu.hm.cs.swt2ss18.wmtipp.mvc.tipps;

import edu.hm.cs.swt2ss18.wmtipp.mvc.DateHelper;


/**
 * MVC-Modell-Klasse für die Spiele aus Sicht eines Mittippers, d.h. ggf. mit
 * einem hinterlegten Tipp.
 * 
 * @author katz.bastian
 *
 */
public class TippEintrag implements Comparable<TippEintrag>{

	long spielId;

	String heimMannschaft;

	String gastMannschaft;
	
	String spielbeginn;

	String ergebnis;
	
	String ergebnisNachNeunzig;

	String tipp;
	
	String tippNachNeunzig;
		
	String turnierStatus;

	int punkte;

	boolean tippbar;
	
	boolean vorbei;

	public long getSpielId() {
		return spielId;
	}

	public void setSpielId(long spielId) {
		this.spielId = spielId;
	}

	public boolean isTippbar() {
		return tippbar;
	}

	public void setTippbar(boolean tippbar) {
		this.tippbar = tippbar;
	}

	public String getHeimMannschaft() {
		return heimMannschaft;
	}

	public void setHeimMannschaft(String heimMannschaft) {
		this.heimMannschaft = heimMannschaft;
	}

	public String getGastMannschaft() {
		return gastMannschaft;
	}

	public void setGastMannschaft(String gastMannschaft) {
		this.gastMannschaft = gastMannschaft;
	}

	public String getErgebnis() {
		return ergebnis;
	}

	public void setErgebnis(String ergebnis) {
		this.ergebnis = ergebnis;
	}
	
	public String getErgebnisNachNeunzig() {
		return ergebnisNachNeunzig;
	}

	public void setErgebnisNachNeunzig(String ergebnisNachNeunzig) {
		this.ergebnisNachNeunzig = ergebnisNachNeunzig;
	}

	public String getTipp() {
		if (tipp == null){
			return "";
		} else {
			return tipp;
		}
		
	}

	public void setTipp(String tipp) {
		this.tipp = tipp;
	}
	
	public String getTippNachNeunzig() {
		return tippNachNeunzig;
	}

	public void setTippNachNeunzig(String tippNachNeunzig) {
		this.tippNachNeunzig = tippNachNeunzig;
	}

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}

	public boolean isVorbei() {
		return vorbei;
	}

	public void setVorbei(boolean vorbei) {
		this.vorbei = vorbei;
	}
	
	public String getSpielbeginn() {
		return spielbeginn;
	}

	public void setSpielbeginn(String spielbeginn) {
		this.spielbeginn = spielbeginn;
	}

	public String getTurnierStatus() {
		return turnierStatus;
	}

	public void setTurnierStatus(String turnierStatus) {
		this.turnierStatus = turnierStatus;
	}

	@Override
	public int compareTo(TippEintrag tippEintrag) {
		DateHelper dateHelper = new DateHelper();
			return dateHelper.stringToDate(this.spielbeginn).compareTo(dateHelper.stringToDate(tippEintrag.getSpielbeginn()));
	}
}
