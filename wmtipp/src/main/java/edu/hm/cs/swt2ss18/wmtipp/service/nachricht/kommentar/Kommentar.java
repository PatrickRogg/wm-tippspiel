package edu.hm.cs.swt2ss18.wmtipp.service.nachricht.kommentar;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.Nachricht;

@Entity
public class Kommentar implements Comparable<Kommentar> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;
	
	LocalDateTime datum;

	@Lob
	String kommentar;
	
	@ManyToOne
	Nachricht nachricht;
	
	String mitspielerAnzeigeName;
	
	public Kommentar() {
		
	}

	public Kommentar(LocalDateTime datum, String kommentar, Mitspieler mitspieler, String mitspielerAnzeigeName) {
		super();
		this.datum = datum;
		this.kommentar = kommentar;
		this.mitspielerAnzeigeName = mitspielerAnzeigeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDatum() {
		return datum;
	}

	public void setDatum(LocalDateTime datum) {
		this.datum = datum;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public String getMitspielerAnzeigeName() {
		return mitspielerAnzeigeName;
	}

	public void setMitspielerAnzeigeName(String mitspielerAnzeigeName) {
		this.mitspielerAnzeigeName = mitspielerAnzeigeName;
	}

	public Nachricht getNachricht() {
		return nachricht;
	}

	public void setNachricht(Nachricht nachricht) {
		this.nachricht = nachricht;
	}

	@Override
	public int compareTo(Kommentar kommentar) {
		return kommentar.getDatum().compareTo(this.datum);
	}
}
