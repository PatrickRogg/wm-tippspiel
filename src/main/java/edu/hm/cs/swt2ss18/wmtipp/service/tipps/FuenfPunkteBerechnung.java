package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

public class FuenfPunkteBerechnung implements PunkteBerechnung{

	private static FuenfPunkteBerechnung instance = null;
	
	private FuenfPunkteBerechnung(){
		
	}
	
	public static FuenfPunkteBerechnung getInstance(){
		if (instance == null) {
			instance = new FuenfPunkteBerechnung();
		}
		return instance;
	}
	
	@Override
	public int berechnePunkte(Tipp tipp) {
		int punkte;
		punkte = 0;
		
		Spiel spiel = tipp.getSpiel();
		if (!spiel.isVorbei()) {
			return 0;
		}
		
		if (spiel.getToreHeimMannschaft() - tipp.getToreHeimMannschaft() == 0) {
			punkte = punkte + 1;
		}
		
		if (spiel.getToreGastMannschaft() - tipp.getToreGastMannschaft() == 0) {
			punkte = punkte + 1;
		}
		
		if (spiel.getToreHeimMannschaftNachNeunzig() - tipp.getToreHeimMannschaftNachNeunzig() == 0) {
			punkte = punkte + 1;
		}
		
		if (spiel.getToreGastMannschaftNachNeunzig() - tipp.getToreGastMannschaftNachNeunzig() == 0) {
			punkte = punkte + 1;
		}
		
		if (spiel.getSpielinfo().equals(tipp.getSpielinfo())) {
			punkte = punkte + 1;
		}
		return punkte;
	}
}