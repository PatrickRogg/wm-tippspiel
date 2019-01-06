package edu.hm.cs.swt2ss18.wmtipp.mvc;

public enum GruppenTyp {
	GRUPPE_A("Gruppe A"),
	GRUPPE_B("Gruppe B"),
	GRUPPE_C("Gruppe C"),
	GRUPPE_D("Gruppe D"),
	GRUPPE_E("Gruppe E"),
	GRUPPE_F("Gruppe F"),
	GRUPPE_G("Gruppe G"),
	GRUPPE_H("Gruppe H"),
	ACHTELFINALE("Achtelfinale"),
	VIERTELFINALE("Viertelfinale"),
	HALBFINALE("Halbfinale"),
	DRITTERPLATZ("Spiel um Platz 3"),
	FINALE("Finale");
    
    private final String gruppe;
    
    GruppenTyp(String gruppe){
    	this.gruppe = gruppe;
    }
    
    public String getGruppe() {
    	return this.gruppe;
    }
    
    public static GruppenTyp excelGruppeToEnum(String gruppePartTwo) {
    	String gruppePartOne = "GRUPPE_";
    	String gruppe = gruppePartOne + gruppePartTwo;
    	
    	return GruppenTyp.valueOf(gruppe);
    }
}
