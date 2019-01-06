package edu.hm.cs.swt2ss18.wmtipp.mvc.ergebnisse;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

public class SpielListe {
	
	private ArrayList<Spiel> spiele;

	public SpielListe() {
		
	}
	
	public SpielListe(List<Spiel> spiele) {
		super();
		this.spiele = new ArrayList<>(spiele);
	}

	public ArrayList<Spiel> getMatches() {
		return spiele;
	}

	public void setMatches(ArrayList<Spiel> spiele) {
		this.spiele = spiele;
	}
}
