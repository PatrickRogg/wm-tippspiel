package edu.hm.cs.swt2ss18.wmtipp.service.tabellen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.mvc.GruppenTyp;
import edu.hm.cs.swt2ss18.wmtipp.mvc.tabellen.TabellenEintrag;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.Team;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamService;

@Service
@Transactional
public class TabellenService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TabellenService.class);
	
	@Autowired
	TabellenRepository tabellenRepository;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	SpielService spielService;
	
	GruppenTyp gruppenTyp;
	
	@PostConstruct
	public synchronized void insertTabellen() {
		if (tabellenRepository.count() == 0) {
			for(GruppenTyp gruppenTyp : GruppenTyp.values()) {
				tabellenRepository.save(new Tabelle(gruppenTyp));
				LOG.info("die Tabelle:  {} wurde erzeugt", gruppenTyp);
			}
		}
		
		teamService.erzeugeTeamsFromExcelFile();
		spielService.erzeugeSpieleFromExcelFile();
	}
	
	public Tabelle findeTabelle(Long tabellenId) {
		return tabellenRepository.getOne(tabellenId);
	}
	
	public List<Tabelle> findeAlleTabellen() {
		return tabellenRepository.findAll();
	}
	
	public Tabelle findeTabelleByGruppe(GruppenTyp gruppenTyp) {
		for(Tabelle tabelle : findeAlleTabellen()) {
			if(tabelle.getGruppe().equals(gruppenTyp)) {
				return tabellenRepository.getOne(tabelle.getId());
			}
		}
		
		return null;
	}
	
	public List<Team> findeAlleTeams(Long tabellenId) {
		List<Team> spiele = new ArrayList<>();
		for(Team team : teamService.findeAlleTeams()) {
			if(team.getGruppenTyp().equals(findeTabelle(tabellenId).getGruppe())) {
				spiele.add(team);
			}
		}
		return  spiele;
	}

	public GruppenTyp getGruppenTyp() {
		return gruppenTyp;
	}

	public void setGruppenTyp(GruppenTyp gruppenTyp) {
		this.gruppenTyp = gruppenTyp;
	}
	
	public boolean isKOPhase(Long tabellenId) {
		if(findeTabelle(tabellenId).getGruppe().equals(GruppenTyp.ACHTELFINALE) || findeTabelle(tabellenId).getGruppe().equals(GruppenTyp.VIERTELFINALE)
				|| findeTabelle(tabellenId).getGruppe().equals(GruppenTyp.HALBFINALE) || findeTabelle(tabellenId).getGruppe().equals(GruppenTyp.DRITTERPLATZ)
				|| findeTabelle(tabellenId).getGruppe().equals(GruppenTyp.FINALE)) {
			return true;
		} else {
			return false;
		}
	}
	
	public int zaehleAktuelleSpieleEinerGruppe(Long tabellenId) {
		int anzahlSpieleDerGruppe = 0;
		for(Spiel spiel : spielService.findeAlleSpiele()) {
			if(spiel.isVorbei()) {
				if(spiel.getHeimMannschaft().getGruppenTyp().equals(findeTabelle(tabellenId).getGruppe()) && 
						spiel.getGastMannschaft().getGruppenTyp().equals(findeTabelle(tabellenId).getGruppe())) {
					anzahlSpieleDerGruppe++;
				}
			}
		}
		
		return anzahlSpieleDerGruppe;
	}
	
	public boolean tabelleIstFertig(Long tabellenId) {
		/*
		 * TODO FAKULTÄT? geht das überhaupt mehr als 4 teams zu haben?
		 */
		
		int gesamteAnzahlDerSpieleEinerGruppe = 0;
		for(Spiel spiel : spielService.findeAlleSpiele()) {
			if(spiel.getHeimMannschaft().getGruppenTyp().equals(findeTabelle(tabellenId).getGruppe())) {
				gesamteAnzahlDerSpieleEinerGruppe++;
			}
		}
		
		if(gesamteAnzahlDerSpieleEinerGruppe <= zaehleAktuelleSpieleEinerGruppe(tabellenId)) {
			return true;
		} else {
			return false;
		}
	}	
	
	public List<TabellenEintrag> erstelleTabelle(Long tabellenId) {
		List<TabellenEintrag> tabellenEinträge = new ArrayList<>();
				
		for(Team team : teamService.findeAlleTeams()) {
			if(team.getGruppenTyp().equals(findeTabelle(tabellenId).getGruppe())) {
				TabellenEintrag eintrag = new TabellenEintrag();
				eintrag.setTeamWappen(team.getWappen());
				eintrag.setTeamName(team.getName());
				eintrag.setSpiele(teamService.getAktuelleAnzahlSpieleGruppenphase(team));
				eintrag.setSiege(teamService.getAktuelleAnzahlSiegeGruppenphase(team));
				eintrag.setUnentschieden(teamService.getAktuelleAnzahlUnentschiedenGruppenphase(team));
				eintrag.setNiederlagen(teamService.getAktuelleAnzahlNiederlagenGruppenphase(team));
				eintrag.setTore(teamService.getAlleTore(team));
				eintrag.setGegentore(teamService.getAlleGegenTore(team));
				eintrag.setTorDifferenz(teamService.getAktuelleTorDifferenzGruppenphase(team));
				eintrag.setPunkte(teamService.getAktuellePunkteGruppenphase(team));
				
				tabellenEinträge.add(eintrag);
			}
		}
		
		
		for(TabellenEintrag team : tabellenEinträge) {
			team.setPlatzierung(teamService.getPlatzierungVonTeamInGruppe(teamService.findeTeamByString(team.getTeamName())));
		}
		
		Collections.sort(tabellenEinträge);
		
		int platzierung = 0;
		for(TabellenEintrag team : tabellenEinträge) {
			platzierung ++;
			team.setPlatzierung(platzierung);
		}
		
		return tabellenEinträge;
	}
}
