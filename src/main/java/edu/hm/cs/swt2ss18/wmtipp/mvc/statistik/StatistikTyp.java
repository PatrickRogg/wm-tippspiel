package edu.hm.cs.swt2ss18.wmtipp.mvc.statistik;

public enum StatistikTyp {
	
	DURCHSCHNITTLICHE_PUNKTE("Durchschnittliche Punkte"),
	ANZAHL_TIPPS("Anzahl Tipps"),
	ANZAHL_VERPASSTER_SPIELE("Anzahl verpasster Spiele");
    
    private final String statistikTyp;
    
    StatistikTyp(String statistikTyp){
    	this.statistikTyp = statistikTyp;
    }
    
    public String getStatistikTyp() {
    	return this.statistikTyp;
    }
}
