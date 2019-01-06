package edu.hm.cs.swt2ss18.wmtipp.mvc.ranking;

/**
 * Modellklasse für Einträge in der Rangliste.
 * 
 * @author katz.bastian
 */
public class RanglistenEintrag implements Comparable<RanglistenEintrag>{

	private String anzeigeName;
	
	private int platzierung;
	
	private int punkte;

	public String getAnzeigeName() {
		return anzeigeName;
	}

	public int getPlatzierung() {
		return platzierung;
	}

	public void setPlatzierung(int platzierung) {
		this.platzierung = platzierung;
	}

	public void setAnzeigeName(String displayName) {
		this.anzeigeName = displayName;
	}

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int points) {
		this.punkte = points;
	}

	@Override
	public int compareTo(RanglistenEintrag compareTo) {
		int comparePunkte=((RanglistenEintrag)compareTo).getPunkte();
		return comparePunkte-this.punkte;
	}
}
