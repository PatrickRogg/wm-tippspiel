package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

public class EinPunkteBerechnung implements PunkteBerechnung{

	private static EinPunkteBerechnung instance = null;
	
	private EinPunkteBerechnung(){
		
	}
	
	public static EinPunkteBerechnung getInstance(){
		if (instance == null) {
			instance = new EinPunkteBerechnung();
		}
		return instance;
	}
	
	@Override
	public int berechnePunkte(Tipp tipp) {
		Spiel spiel = tipp.getSpiel();
		if (!spiel.isVorbei()) {
			return 0;
		}
		
		if (Integer.signum(spiel.getToreHeimMannschaft() - spiel.getToreGastMannschaft()) == 
				Integer.signum(tipp.getToreHeimMannschaft() - tipp.getToreGastMannschaft())) {
			return 1;
		}
		return 0;
	}
}
