package edu.hm.cs.swt2ss18.wmtipp.mvc.tabellen;

public class TabellenEintrag implements Comparable<TabellenEintrag> {
	
	private int platzierung;
	
	private String teamName;
	
	private String teamWappen;
	
	private int siege;
	
	private int unentschieden;
	
	private int niederlagen;
	
	private int spiele = siege + unentschieden + niederlagen;
	
	private int tore;
	
	private int gegentore;
	
	private int torDifferenz = tore - gegentore;
	
	private int punkte;
	
	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}

	public int getPlatzierung() {
		return platzierung;
	}

	public void setPlatzierung(int platzierung) {
		this.platzierung = platzierung;
	}

	public int getTore() {
		return tore;
	}

	public void setTore(int tore) {
		this.tore = tore;
	}

	public int getGegentore() {
		return gegentore;
	}

	public void setGegentore(int gegentore) {
		this.gegentore = gegentore;
	}

	public int getTorDifferenz() {
		return torDifferenz;
	}

	public void setTorDifferenz(int torDifferenz) {
		this.torDifferenz = torDifferenz;
	}

	public int getSiege() {
		return siege;
	}

	public void setSiege(int siege) {
		this.siege = siege;
	}

	public int getUnentschieden() {
		return unentschieden;
	}

	public void setUnentschieden(int unentschieden) {
		this.unentschieden = unentschieden;
	}

	public int getNiederlagen() {
		return niederlagen;
	}

	public void setNiederlagen(int niederlagen) {
		this.niederlagen = niederlagen;
	}

	public int getSpiele() {
		return spiele;
	}

	public void setSpiele(int spiele) {
		this.spiele = spiele;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamWappen() {
		return teamWappen;
	}

	public void setTeamWappen(String teamWappen) {
		this.teamWappen = teamWappen;
	}

	@Override
	public int compareTo(TabellenEintrag compareTo) {
		return this.platzierung - compareTo.getPlatzierung();
	}
}
