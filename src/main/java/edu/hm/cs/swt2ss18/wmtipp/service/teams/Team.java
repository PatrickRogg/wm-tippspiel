package edu.hm.cs.swt2ss18.wmtipp.service.teams;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import edu.hm.cs.swt2ss18.wmtipp.mvc.GruppenTyp;

@Entity
public class Team {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	Long id;

	String name;
	
	String wappen;
	
	GruppenTyp gruppenTyp;

	public Team(String name, String wappen, GruppenTyp gruppenTyp) {
		this.name = name;
		this.wappen = wappen;
		this.gruppenTyp = gruppenTyp;
	}
	
	public Team() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWappen() {
		return wappen;
	}

	public void setWappen(String wappen) {
		this.wappen = wappen;
	}

	public GruppenTyp getGruppenTyp() {
		return gruppenTyp;
	}

	public void setGruppenTyp(GruppenTyp gruppenTyp) {
		this.gruppenTyp = gruppenTyp;
	}
}
