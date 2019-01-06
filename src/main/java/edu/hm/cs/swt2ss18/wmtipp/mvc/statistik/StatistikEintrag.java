package edu.hm.cs.swt2ss18.wmtipp.mvc.statistik;

public class StatistikEintrag implements Comparable<StatistikEintrag>{
	
	private String anzeigeName;
	
	private int platzierung;
	
	private float punkte;

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
	
	public float getPunkte() {
		return punkte;
	}

	public void setPunkte(float points) {
		this.punkte = points;
	}

	@Override
	public int compareTo(StatistikEintrag compareTo) {
		float comparePunkte=((StatistikEintrag)compareTo).getPunkte();
		
		if(comparePunkte-this.punkte > 0) {
			return 1;
		} 
		
		if(comparePunkte-this.punkte < 0) {
			return -1;
		}
		return 0;
	}
}
