package edu.hm.cs.swt2ss18.wmtipp.service.spiele;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.ValidationException;
import javax.validation.constraints.Digits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.hm.cs.swt2ss18.wmtipp.mvc.SpielinfoTyp;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.Team;


@Entity
public class Spiel implements Comparable<Spiel>{

	private static final Logger LOG = LoggerFactory.getLogger(Spiel.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;

	@ManyToOne
	Team heimMannschaft;
	
	@ManyToOne
	Team gastMannschaft;

	boolean tippbar = true;

	boolean vorbei = false;

	@Digits(integer=4,fraction=0)
	int toreHeimMannschaft;
	
	@Digits(integer=4,fraction=0)
	int toreGastMannschaft;
	
	@Digits(integer=4,fraction=0)
	int toreHeimMannschaftNachNeunzig;

	@Digits(integer=4,fraction=0)
	int toreGastMannschaftNachNeunzig;
	
	SpielinfoTyp spielinfo = SpielinfoTyp.STANDARD;
	
	TurnierStatus turnierStatus;
	
	LocalDateTime spielbeginn;
	
	String spielort;
	
	public Spiel(Team heimMannschaft, Team gastMannschaft, LocalDateTime spielbeginn, TurnierStatus turnierStatus, String spielort) {
		super();
		this.heimMannschaft = heimMannschaft;
		this.gastMannschaft = gastMannschaft;
		this.spielbeginn = spielbeginn;
		this.turnierStatus = turnierStatus;
		this.spielort = spielort;
	}

	public Spiel() {
		// nicht entfernen! Wird für JPA benötigt!
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Team getHeimMannschaft() {
		return heimMannschaft;
	}

	public void setHeimMannschaft(Team heimMannschaft) {
		this.heimMannschaft = heimMannschaft;
	}

	public Team getGastMannschaft() {
		return gastMannschaft;
	}

	public void setGastMannschaft(Team gastMannschaft) {
		this.gastMannschaft = gastMannschaft;
	}

	public boolean isTippbar() {
		return tippbar;
	}

	public void setTippbar(boolean tippbar) {
		if (vorbei && tippbar) {
			throw new ValidationException("Abgeschlossene Spiele können nicht zum Tippen freigegeben werden!");
		}
		LOG.info("Das Spiel: {} : {}  wurde auf nicht mehr Tippbar gesetzt.", getHeimMannschaft().getName(), getGastMannschaft().getName());
		this.tippbar = tippbar;
	}

	public boolean isVorbei() {
		return vorbei;
	}

	public void setVorbei(boolean vorbei) {
		this.vorbei = vorbei;
		if (vorbei) {
			setTippbar(false);
		}
	}

	public int getToreHeimMannschaft() {
		return toreHeimMannschaft;
	}

	public void setToreHeimMannschaft(int toreHeimMannschaft) {
		this.toreHeimMannschaft = toreHeimMannschaft;
	}

	public int getToreGastMannschaft() {
		return toreGastMannschaft;
	}

	public void setToreGastMannschaft(int toreGastMannschaft) {
		this.toreGastMannschaft = toreGastMannschaft;
	}
	
	public int getToreHeimMannschaftNachNeunzig() {
		return toreHeimMannschaftNachNeunzig;
	}

	public void setToreHeimMannschaftNachNeunzig(int toreHeimMannschaftNachNeunzig) {
		this.toreHeimMannschaftNachNeunzig = toreHeimMannschaftNachNeunzig;
	}

	public int getToreGastMannschaftNachNeunzig() {
		return toreGastMannschaftNachNeunzig;
	}

	public void setToreGastMannschaftNachNeunzig(int toreGastMannschaftNachNeunzig) {
		this.toreGastMannschaftNachNeunzig = toreGastMannschaftNachNeunzig;
	}
	
	public SpielinfoTyp getSpielinfo() {
		return spielinfo;
	}

	public void setSpielinfo(SpielinfoTyp spielinfo) {
		this.spielinfo = spielinfo;
	}
	
	public LocalDateTime getSpielbeginn() {
		return spielbeginn;
	}

	public void setSpielbeginn(LocalDateTime spielbeginn) {
		this.spielbeginn = spielbeginn;
	}
	
	public TurnierStatus getTurnierStatus() {
		return turnierStatus;
	}

	public void setTurnierStatus(TurnierStatus turnierStatus) {
		this.turnierStatus = turnierStatus;
	}
	
	public String getSpielort() {
		return spielort;
	}

	public void setSpielort(String spielort) {
		this.spielort = spielort;
	}

	@Transient
	public String getErgebnis() {
		if(getSpielinfo().getSpielinfoTyp().equals("---")){
			return vorbei ? (toreHeimMannschaft + ":" + toreGastMannschaft) : "--:--";
		} else {
			return vorbei ? (toreHeimMannschaft + ":" + toreGastMannschaft + " " + getSpielinfo().getSpielinfoTyp()) : "--:--";
		}
	}
	
	@Transient
	public String getErgebnisNachNeunzig() {
		if(getSpielinfo().getSpielinfoTyp().equals("---")){
			return "";
		} else {
			return vorbei ? ("(" + toreHeimMannschaftNachNeunzig + ":" + toreGastMannschaftNachNeunzig + ")") : "--:--";
		}
	}

	@Override
	public int compareTo(Spiel spiel) {
		return this.spielbeginn.compareTo(spiel.getSpielbeginn());
	}
}
