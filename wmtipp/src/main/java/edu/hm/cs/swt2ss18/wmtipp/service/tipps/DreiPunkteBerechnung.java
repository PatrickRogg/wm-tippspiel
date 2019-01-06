package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

public class DreiPunkteBerechnung implements PunkteBerechnung{
	private static DreiPunkteBerechnung instance = null;
	
	private DreiPunkteBerechnung(){
		
	}
	
	public static DreiPunkteBerechnung getInstance(){
		if (instance == null) {
			instance = new DreiPunkteBerechnung();
		}
		return instance;
	}
	
	@Override
	public int berechnePunkte(Tipp tipp) {
		Spiel spiel = tipp.getSpiel();
		if (!spiel.isVorbei()) {
			return 0;
		}
		
		if (spiel.getToreHeimMannschaft() - tipp.getToreHeimMannschaft() == 0 && 
			spiel.getToreGastMannschaft() - tipp.getToreGastMannschaft() == 0) {
			return 3;
		}
		
		if (spiel.getToreHeimMannschaft() - spiel.getToreGastMannschaft() == 
			tipp.getToreHeimMannschaft() - tipp.getToreGastMannschaft()) {
			return 2;
		}
		
		if (Integer.signum(spiel.getToreHeimMannschaft() - spiel.getToreGastMannschaft()) == 
				Integer.signum(tipp.getToreHeimMannschaft() - tipp.getToreGastMannschaft())) {
			return 1;
		}
		return 0;
	}
}
