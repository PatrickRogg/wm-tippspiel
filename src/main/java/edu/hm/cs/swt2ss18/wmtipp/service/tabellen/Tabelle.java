package edu.hm.cs.swt2ss18.wmtipp.service.tabellen;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import edu.hm.cs.swt2ss18.wmtipp.mvc.GruppenTyp;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.Team;

@Entity
public class Tabelle {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	Long id;
	
	@ManyToOne
	Team team;
	
	GruppenTyp gruppe;

	public Tabelle(GruppenTyp gruppenTyp) {
		this.gruppe = gruppenTyp;
	}
	
	public Tabelle() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public GruppenTyp getGruppe() {
		return gruppe;
	}

	public void setGruppe(GruppenTyp gruppe) {
		this.gruppe = gruppe;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
