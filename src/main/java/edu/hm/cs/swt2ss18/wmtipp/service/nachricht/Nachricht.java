package edu.hm.cs.swt2ss18.wmtipp.service.nachricht;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;

@Entity
public class Nachricht implements Comparable<Nachricht>{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;
	
	LocalDateTime datum;
	
	String ueberschrift;
	
	@Lob
	String nachricht;
	
	@ElementCollection
	private Set<String> likes = new HashSet<>();

	String mitspielerAnzeigeName;
	
	public Nachricht() {
		
	}

	public Nachricht(LocalDateTime datum, String nachricht, String ueberschrift, String mitspielerAnzeigeName) {
		super();
		this.datum = datum;
		this.nachricht = nachricht;
		this.ueberschrift = ueberschrift;
		this.mitspielerAnzeigeName = mitspielerAnzeigeName;
	}

	public String getUeberschrift() {
		return ueberschrift;
	}

	public void setUeberschrift(String überschrift) {
		this.ueberschrift = überschrift;
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

	public String getNachricht() {
		return nachricht;
	}

	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}

	public String getMitspielerAnzeigeName() {
		return mitspielerAnzeigeName;
	}

	public void setMitspielerAnzeigeName(String mitspielerAnzeigeName) {
		this.mitspielerAnzeigeName = mitspielerAnzeigeName;
	}

	public Set<String> getLikes() {
		return likes;
	}

	public void setLikes(Set<String> likes) {
		this.likes = likes;
	}
	
	public void addLiker(Mitspieler liker) {
		getLikes().add(liker.getLogin());
	}

	@Override
	public int compareTo(Nachricht nachricht) {
		return nachricht.getDatum().compareTo(this.datum);
	}
}
