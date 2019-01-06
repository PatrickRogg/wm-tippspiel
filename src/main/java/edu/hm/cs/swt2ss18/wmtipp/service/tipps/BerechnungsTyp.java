package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

public enum BerechnungsTyp {
	EIN_PUNKTE_BERECHNUNG("Ein Punkte Berechnung"),
    DREI_PUNKTE_BERECHNUNG("Drei Punkte Berechnung"),
    FUENF_PUNKTE_BERECHNUNG("Fünf Punkte Berechnung");
    
    private final String berechnungsTyp;
    
    BerechnungsTyp(String berechnungsTyp){
    	this.berechnungsTyp = berechnungsTyp;
    }
    
    public String getBerechnungsTyp() {
    	return this.berechnungsTyp;
    }
}
