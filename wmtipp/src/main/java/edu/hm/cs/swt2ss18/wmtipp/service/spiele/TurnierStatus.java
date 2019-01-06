package edu.hm.cs.swt2ss18.wmtipp.service.spiele;

public enum TurnierStatus {
	GRUPPENPHASE("Gruppenphase"),
	ACHTELFINALE_EINS("Achtelfinale 1"),
	ACHTELFINALE_ZWEI("Achtelfinale 2"),
	ACHTELFINALE_DREI("Achtelfinale 3"),
	ACHTELFINALE_VIER("Achtelfinale 4"),
	ACHTELFINALE_FUENF("Achtelfinale 5"),
	ACHTELFINALE_SECHS("Achtelfinale 6"),
	ACHTELFINALE_SIEBEN("Achtelfinale 7"),
	ACHTELFINALE_ACHT("Achtelfinale 8"),
	VIERTELFINALE_EINS("Viertelfinale 1"),
	VIERTELFINALE_ZWEI("Viertelfinale 2"),
	VIERTELFINALE_DREI("Viertelfinale 3"),
	VIERTELFINALE_VIER("Viertelfinale 4"),
	HALBFINALE_EINS("Halbfinale 1"),
	HALBFINALE_ZWEI("Halbfinale 2"),
	DRITTERPLATZ("Spiel um Platz 3"),
	FINALE("Finale");
    
    private final String turnierStatus;
    
    TurnierStatus(String turnierStatus){
    	this.turnierStatus = turnierStatus;
    }
    
    public String getTurnierStatus() {
    	return this.turnierStatus;
    }
}
