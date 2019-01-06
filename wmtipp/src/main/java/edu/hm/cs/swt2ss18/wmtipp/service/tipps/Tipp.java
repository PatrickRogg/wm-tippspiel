package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;

import edu.hm.cs.swt2ss18.wmtipp.mvc.SpielinfoTyp;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

@Entity
public class Tipp {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	Long id;
	
	@ManyToOne
	Mitspieler mitspieler;
	
	@ManyToOne
	Spiel spiel;
	
	@Digits(integer=4,fraction=0)
	int toreHeimMannschaft;
	
	@Digits(integer=4,fraction=0)
	int toreGastMannschaft;
	
	@Digits(integer=4,fraction=0)
	int toreHeimMannschaftNachNeunzig;
	
	@Digits(integer=4,fraction=0)
	int toreGastMannschaftNachNeunzig;
	
	SpielinfoTyp spielinfo = SpielinfoTyp.STANDARD;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Mitspieler getMitspieler() {
		return mitspieler;
	}

	public void setMitspieler(Mitspieler mitspieler) {
		this.mitspieler = mitspieler;
	}

	public Spiel getSpiel() {
		return spiel;
	}

	public void setSpiel(Spiel match) {
		this.spiel = match;
	}

	public int getToreHeimMannschaft() {
		return toreHeimMannschaft;
	}

	public void setToreHeimMannschaft(Integer toreHeimMannschaft) {
		this.toreHeimMannschaft = toreHeimMannschaft;
	}

	public int getToreGastMannschaft() {
		return toreGastMannschaft;
	}

	public void setToreGastMannschaft(Integer toreGastMannschaft) {
		this.toreGastMannschaft = toreGastMannschaft;
	}
	
	public int getToreHeimMannschaftNachNeunzig() {
		return toreHeimMannschaftNachNeunzig;
	}

	public void setToreHeimMannschaftNachNeunzig(Integer toreHeimMannschaftNachNeunzig) {
		this.toreHeimMannschaftNachNeunzig = toreHeimMannschaftNachNeunzig;
	}

	public int getToreGastMannschaftNachNeunzig() {
		return toreGastMannschaftNachNeunzig;
	}

	public void setToreGastMannschaftNachNeunzig(Integer toreGastMannschaftNachNeunzig) {
		this.toreGastMannschaftNachNeunzig = toreGastMannschaftNachNeunzig;
	}
	
	public SpielinfoTyp getSpielinfo() {
		return spielinfo;
	}

	public void setSpielinfo(SpielinfoTyp spielinfo) {
		this.spielinfo = spielinfo;
	}
	
	@Transient
	public String getErgebnis() {

		if(getSpielinfo().getSpielinfoTyp().equals("---")){
			return toreHeimMannschaft + ":" + toreGastMannschaft;
		} else {
			return toreHeimMannschaft + ":" + toreGastMannschaft + " " + getSpielinfo().getSpielinfoTyp();
		}
	}
	
	@Transient
	public String getErgebnisNachNeunzig() {
		if(getSpielinfo().getSpielinfoTyp().equals("---")){
			return "";
		} else {
			return "(" + toreHeimMannschaftNachNeunzig + ":" + toreGastMannschaftNachNeunzig + ")";
		}
	}
	
}
