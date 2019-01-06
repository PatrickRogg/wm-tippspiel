package edu.hm.cs.swt2ss18.wmtipp.service.teams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.mvc.ExcelReader;
import edu.hm.cs.swt2ss18.wmtipp.mvc.GruppenTyp;
import edu.hm.cs.swt2ss18.wmtipp.mvc.tabellen.TabellenEintrag;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.TurnierStatus;
import edu.hm.cs.swt2ss18.wmtipp.service.tabellen.TabellenService;

@Service
@Transactional
public class TeamService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TeamService.class);
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	TabellenService tabellenService;
	
	@Autowired
	SpielService spielService;

	
	public Team findeTeam(Long id) {
		return teamRepository.getOne(id);
	}
	
	public Team findeTeamByString(String teamName) {
		for(Team team : findeAlleTeams()) {
			if(team.getName().equalsIgnoreCase(teamName)) {
				return team;
			}
		}
		return null;
	}

	public List<Team> findeAlleTeams() {
		return teamRepository.findAll();
	}
	
	public List<Team> findeAlleTeamsVonEinerGruppe(GruppenTyp gruppenTyp) {
		List<Team> teamsDerGruppe = new ArrayList<>();
		
		for(Team team : findeAlleTeams()) {
			if(team.getGruppenTyp().equals(gruppenTyp)) {
				teamsDerGruppe.add(team);
			}
		}
		return teamsDerGruppe;
	}
	
	public void neuesTeam(Team team) {
		teamRepository.save(team);
	}
	
	public void löscheTeam(Team team) {
		teamRepository.delete(team);
	}
	
	public void löscheTeamByTeamName(String teamName) {
		for(Team team : findeAlleTeams()) {
			if(team.getName().equals(teamName)) {
				teamRepository.delete(team);
			}
		}
	}
	
	public List<Spiel> findeAlleSpiele(Team team) {
		List<Spiel> spiele = new ArrayList<Spiel>();
		for(Spiel spiel : spielService.findeAlleSpiele()) {
			if(spiel.getGastMannschaft() == team || spiel.getHeimMannschaft() == team) {
				spiele.add(spiel);
			}
		}
		return spiele;
	}
	
	
	
	public int getAlleGegenTore(Team team) {
		int gegentore = 0;
		
		for(Spiel spiel : spielService.findeAlleSpiele()) {
			if(spiel.getGastMannschaft() == team) {
				gegentore = gegentore + spiel.getToreHeimMannschaft();
			}
			
			if(spiel.getHeimMannschaft() == team) {
				gegentore = gegentore + spiel.getToreGastMannschaft();
			}
		}
		
		return gegentore;
	}
	
	public int getAlleTore(Team team) {
		int tore = 0;
		
		for(Spiel spiel : spielService.findeAlleSpiele()) {
			if(spiel.getGastMannschaft() == team) {
				tore = tore + spiel.getToreGastMannschaft();
			}
			
			if(spiel.getHeimMannschaft() == team) {
				tore = tore + spiel.getToreHeimMannschaft();
			}
		}
		
		return tore;
	}
	
	public int getAktuelleAnzahlSpieleGruppenphase(Team team) {
		int spiele = 0;
		
		for(Spiel spiel : findeAlleSpiele(team)) {
			if(spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE) && spiel.isVorbei()) {
				spiele++;
			}
		}
		
		return spiele;
	}
	
	public int getAktuelleAnzahlSiegeGruppenphase(Team team) {
		int siege = 0;
		
		for(Spiel spiel : findeAlleSpiele(team)) {
			if(spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
				if(spiel.getGastMannschaft() == team) {
					if(spiel.getToreHeimMannschaft() < spiel.getToreGastMannschaft()) {
						siege++;
					}
				}
				
				if(spiel.getHeimMannschaft() == team) {
					if(spiel.getToreHeimMannschaft() > spiel.getToreGastMannschaft()) {
						siege++;
					}
				}
			}
		}
		
		return siege;
	}
	
	public int getAktuelleAnzahlUnentschiedenGruppenphase(Team team) {
		int unentschieden = 0;
		
		for(Spiel spiel : findeAlleSpiele(team)) {
			if(spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
				if(spiel.getErgebnis() == "--:--") {
					
				} else {
					if(spiel.getToreHeimMannschaft() == spiel.getToreGastMannschaft()) {
						unentschieden++;
					}
				}
			}
		}
		
		return unentschieden;
	}
	
	public int getAktuelleAnzahlNiederlagenGruppenphase(Team team) {
		int niederlagen = 0;
		
		for(Spiel spiel : findeAlleSpiele(team)) {
			if(spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
				if(spiel.getGastMannschaft() == team) {
					if(spiel.getToreHeimMannschaft() > spiel.getToreGastMannschaft()) {
						niederlagen++;
					}
				}
				
				if(spiel.getHeimMannschaft() == team) {
					if(spiel.getToreHeimMannschaft() < spiel.getToreGastMannschaft()) {
						niederlagen++;
					}
				}
			}
		}
		
		return niederlagen;
	}
	
	public int getAktuelleTorDifferenzGruppenphase(Team team) {
		int torDifferenz = 0;
		
		for(Spiel spiel : findeAlleSpiele(team)) {
			if(spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
				if(spiel.getGastMannschaft() == team) {
					torDifferenz = torDifferenz + (spiel.getToreGastMannschaft() - spiel.getToreHeimMannschaft());
				}
				
				if(spiel.getHeimMannschaft() == team) {
					torDifferenz = torDifferenz + (spiel.getToreHeimMannschaft() - spiel.getToreGastMannschaft());
				}
			}
		}
		
		return torDifferenz;
	}
	
	public int getAktuellePunkteGruppenphase(Team team) {
		int punkte = 0;
		
		punkte = punkte + 3 * getAktuelleAnzahlSiegeGruppenphase(team);
		punkte = punkte + 1 * getAktuelleAnzahlUnentschiedenGruppenphase(team);
		return punkte;
	}
	
	public void erzeugeTeamsFromExcelFile() {
		//erstellt Teams anhand der ExcelTabelle
		try {
			ExcelReader excelReader = new ExcelReader(0);
			if (teamRepository.count() == 0) {
				for(int i = 1; i < excelReader.getRows(); i++) {
					teamRepository.save(new Team(excelReader.getTeamName(i), excelReader.getWappen(i), excelReader.getGruppenTyp(i)));
					LOG.info("Das Team {} wurde erzeugt", excelReader.getTeamName(i));
				}
				
			}
			
			ExcelReader excelReader2 = new ExcelReader(2);
				for(int i = 1; i < excelReader2.getRows(); i++) {
					teamRepository.save(new Team(excelReader2.getTeamName(i), excelReader2.getWappen(i), excelReader2.getGruppenTyp(i)));
					LOG.info("Das Platzhalterteam {} wurde erzeugt", excelReader2.getTeamName(i));
				}
		} catch (IOException e) {
			LOG.info("Es wurde die Exception: {} geworfen. Es trat ein Fehler beim einlesen der Excelfiles auf.", e);
		}
	}
	
	public Team getSiegerKOSpiel(Spiel spiel) {
		if(spiel.getToreGastMannschaft() > spiel.getToreHeimMannschaft()) {
			return spiel.getGastMannschaft();
		} else {
			return spiel.getHeimMannschaft();
		}
	}
	
	private Team getVerliererKOSpiel(Spiel spiel) {
		if(spiel.getToreGastMannschaft() < spiel.getToreHeimMannschaft()) {
			return spiel.getGastMannschaft();
		} else {
			return spiel.getHeimMannschaft();
		}
	}
	
	public int getPlatzierungVonTeamInGruppe(Team teamPlatzierungBerechnen) {
		int platzierung = 1;
		
		for(Team team : findeAlleTeamsVonEinerGruppe(teamPlatzierungBerechnen.getGruppenTyp())) {
			if(!team.getName().equals(teamPlatzierungBerechnen.getName())) {
				//Punkte
				if(getAktuellePunkteGruppenphase(team) > getAktuellePunkteGruppenphase(teamPlatzierungBerechnen)) {
					platzierung++;
				}
				if(getAktuellePunkteGruppenphase(team) == getAktuellePunkteGruppenphase(teamPlatzierungBerechnen)) {
					//Tordifferenz
					if(getAktuelleTorDifferenzGruppenphase(team) > getAktuelleTorDifferenzGruppenphase(teamPlatzierungBerechnen)) {
						platzierung++;
					}
					if(getAktuelleTorDifferenzGruppenphase(team) == getAktuelleTorDifferenzGruppenphase(teamPlatzierungBerechnen)) {
						//Tore
						if(getAlleTore(team) > getAlleTore(teamPlatzierungBerechnen)) {
							platzierung++;
						}
						if(getAlleTore(team) == getAlleTore(teamPlatzierungBerechnen)) {
							//Direkter Vergleich
							Spiel spiel = spielService.findeSpiel(team.getName(), teamPlatzierungBerechnen.getName());
							if(spiel != null) {
								if(spiel.getToreHeimMannschaft() == spiel.getToreGastMannschaft()) {
									
								}
								if(spiel.getHeimMannschaft().getName().equals(team.getName())) {
									if(spiel.getToreHeimMannschaft() > spiel.getToreGastMannschaft()) {
										platzierung++;
									}
								} else {
									if(spiel.getToreHeimMannschaft() < spiel.getToreGastMannschaft()) {
										platzierung++;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return platzierung;
	}

	public void updateGruppenTypenFürFertigeGruppe(Long tabellenId) {
		for(TabellenEintrag tabellenEintrag : tabellenService.erstelleTabelle(tabellenId)) {
			for(Team team : tabellenService.findeAlleTeams(tabellenId)) {
				if(tabellenEintrag.getTeamName().equals(team.getName())) {
					if(tabellenEintrag.getPlatzierung() == 1) {
						switch (team.getGruppenTyp()) {
						case GRUPPE_A:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ZWEI).getHeimMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ZWEI).getHeimMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ZWEI), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ZWEI).getHeimMannschaft().getName());
							break;
							
						case GRUPPE_B:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_DREI).getHeimMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_DREI).getHeimMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_DREI), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_DREI).getHeimMannschaft().getName());
							break;
							
						case GRUPPE_C:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_EINS).getHeimMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_EINS).getHeimMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_EINS), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_EINS).getHeimMannschaft().getName());
							break;
							
						case GRUPPE_D:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_VIER).getHeimMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_VIER).getHeimMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_VIER), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_VIER).getHeimMannschaft().getName());
							break;
						
						case GRUPPE_E:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_FUENF).getHeimMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_FUENF).getHeimMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_FUENF), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_FUENF).getHeimMannschaft().getName());
							break;
						
						case GRUPPE_F:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SIEBEN).getHeimMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SIEBEN).getHeimMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SIEBEN), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SIEBEN).getHeimMannschaft().getName());
							break;
						
						case GRUPPE_G:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SECHS).getHeimMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SECHS).getHeimMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SECHS), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SECHS).getHeimMannschaft().getName());
							break;
							
						case GRUPPE_H:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ACHT).getHeimMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ACHT).getHeimMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ACHT), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ACHT).getHeimMannschaft().getName());
							break;
						
						default:
							break;
						}
					}
					
					if(tabellenEintrag.getPlatzierung() == 2) {
						switch (team.getGruppenTyp()) {
						case GRUPPE_A:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_DREI).getGastMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_DREI).getGastMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_DREI), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_DREI).getGastMannschaft().getName());
							break;
							
						case GRUPPE_B:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ZWEI).getGastMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ZWEI).getGastMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ZWEI), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ZWEI).getGastMannschaft().getName());
							break;
							
						case GRUPPE_C:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_VIER).getGastMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_VIER).getGastMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_VIER), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_VIER).getGastMannschaft().getName());
							break;
							
						case GRUPPE_D:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_EINS).getGastMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_EINS).getGastMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_EINS), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_EINS).getGastMannschaft().getName());
							break;
						
						case GRUPPE_E:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SIEBEN).getGastMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SIEBEN).getGastMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SIEBEN), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SIEBEN).getGastMannschaft().getName());
							break;
						
						case GRUPPE_F:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_FUENF).getGastMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_FUENF).getGastMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_FUENF), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_FUENF).getGastMannschaft().getName());
							break;
						
						case GRUPPE_G:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ACHT).getGastMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ACHT).getGastMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ACHT), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_ACHT).getGastMannschaft().getName());
							break;
							
						case GRUPPE_H:
							LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SECHS).getGastMannschaft().getName());
							überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SECHS).getGastMannschaft(), team);
							LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SECHS), 
									spielService.findeSpielBySpielStatus(TurnierStatus.ACHTELFINALE_SECHS).getGastMannschaft().getName());
							break;
							
						default:
							break;
						}
					}
				}
			}
		}
		
	}

	private void überschreibeTeamFürKOPhase(Team dummyTeamKO, Team teamGruppenPhase) {
		dummyTeamKO.setName(teamGruppenPhase.getName());
		dummyTeamKO.setWappen(teamGruppenPhase.getWappen());
	}

	public void updateGruppenTypenFürFertigesKOSpiel(Long tabellenId) {
		for(Spiel spiel : spielService.findeAlleSpiele()) {
			if(!spiel.getErgebnis().equals("--:--")) {
				switch (spiel.getTurnierStatus()) {
				case ACHTELFINALE_EINS:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS).getHeimMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS).getHeimMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS), 
							spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS).getHeimMannschaft().getName());
					break;
					
				case ACHTELFINALE_ZWEI:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS).getGastMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS).getGastMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS), 
							spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS).getGastMannschaft().getName());
					break;
					
				case ACHTELFINALE_DREI:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_VIER).getHeimMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_VIER).getHeimMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_VIER), 
							spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_VIER).getHeimMannschaft().getName());
					break;
					
				case ACHTELFINALE_VIER:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_VIER).getGastMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_VIER).getGastMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_VIER), 
							spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_VIER).getGastMannschaft().getName());
					break;
				
				case ACHTELFINALE_FUENF:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_ZWEI).getHeimMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_ZWEI).getHeimMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_ZWEI), 
							spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_ZWEI).getHeimMannschaft().getName());
					break;
				
				case ACHTELFINALE_SECHS:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_ZWEI).getGastMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_ZWEI).getGastMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_ZWEI), 
							spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_ZWEI).getGastMannschaft().getName());
					break;
				
				case ACHTELFINALE_SIEBEN:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_DREI).getHeimMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_DREI).getHeimMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_DREI), 
							spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_DREI).getHeimMannschaft().getName());
					break;
					
				case ACHTELFINALE_ACHT:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_DREI).getGastMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_DREI).getGastMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_DREI), 
							spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_DREI).getGastMannschaft().getName());
					break;
				
				case VIERTELFINALE_EINS:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_EINS).getHeimMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_EINS).getHeimMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.VIERTELFINALE_EINS), 
							spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_EINS).getHeimMannschaft().getName());
					break;
					
				case VIERTELFINALE_ZWEI:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_EINS).getGastMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_EINS).getGastMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_EINS), 
							spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_EINS).getGastMannschaft().getName());
					break;
					
				case VIERTELFINALE_DREI:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_ZWEI).getHeimMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_ZWEI).getHeimMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_ZWEI), 
							spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_ZWEI).getHeimMannschaft().getName());
					break;
					
				case VIERTELFINALE_VIER:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_ZWEI).getGastMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_ZWEI).getGastMannschaft(), getSiegerKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_ZWEI), 
							spielService.findeSpielBySpielStatus(TurnierStatus.HALBFINALE_ZWEI).getGastMannschaft().getName());
					break;
					
				case HALBFINALE_EINS:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.FINALE).getHeimMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.FINALE).getHeimMannschaft(), getSiegerKOSpiel(spiel));
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.DRITTERPLATZ).getHeimMannschaft(), getVerliererKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.FINALE), 
							spielService.findeSpielBySpielStatus(TurnierStatus.FINALE).getHeimMannschaft().getName());
					break;
					
				case HALBFINALE_ZWEI:
					LOG.info("Das Team {} wurde geholt", spielService.findeSpielBySpielStatus(TurnierStatus.FINALE).getGastMannschaft().getName());
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.FINALE).getGastMannschaft(), getSiegerKOSpiel(spiel));
					überschreibeTeamFürKOPhase(spielService.findeSpielBySpielStatus(TurnierStatus.DRITTERPLATZ).getGastMannschaft(), getVerliererKOSpiel(spiel));
					LOG.info("Das Achtelfinale {} wurde in das Team {} geändert", spielService.findeSpielBySpielStatus(TurnierStatus.FINALE), 
							spielService.findeSpielBySpielStatus(TurnierStatus.FINALE).getGastMannschaft().getName());
					break;
				default:
					break;

				}
			}
		}
	}
}
