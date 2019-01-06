package edu.hm.cs.swt2ss18.wmtipp.mvc;

public enum SpielinfoTyp {
	STANDARD("---"),
    VERLAENGERUNG("n.V."),
    ELFMETER("n.E.");
    
    private final String spielinfoName;
    
    SpielinfoTyp(String spielinfoName){
    	this.spielinfoName = spielinfoName;
    }
    
    public String getSpielinfoTyp() {
    	return this.spielinfoName;
    }
}
