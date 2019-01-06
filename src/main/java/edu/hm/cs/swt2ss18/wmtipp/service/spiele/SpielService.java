package edu.hm.cs.swt2ss18.wmtipp.service.spiele;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.mvc.ExcelReader;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.ErgebnisNichtZulaessigException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KOSpielDarfNichtUnentschiedenEndenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinGewinnerException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinUnentschiedenBeiVerlaengerungException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.MannschaftsNameIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.TorEingabeException;
import edu.hm.cs.swt2ss18.wmtipp.service.tabellen.TabellenService;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamService;

@Service
@Transactional
public class SpielService {
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	TabellenService tabellenService;
	
	@Autowired
	private SpielRepository spielRepository;

	private static final Logger LOG = LoggerFactory.getLogger(SpielService.class);
	
	LocalDate localDate = LocalDate.of(2018, 4 , 26);
	LocalTime localTime = LocalTime.of(18, 30);
	LocalDateTime spielbeginn = LocalDateTime.of(localDate, localTime);


	public Spiel findeSpiel(Long id) {
		return spielRepository.getOne(id);
	}
	
	public Spiel findeSpielBySpielStatus(TurnierStatus spielstatus) {
		for(Spiel spiel : findeAlleSpiele()) {
			if(spiel.getTurnierStatus().equals(spielstatus)) {
				return spiel;
			}
		}
		
		return null;
	}

	public List<Spiel> findeAlleSpiele() {
		return spielRepository.findAll();
	}

	public void setzeErgebnis(Spiel spiel) throws TorEingabeException, KeinGewinnerException, KeinUnentschiedenBeiVerlaengerungException, 
	ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException {
		if(spiel.getToreHeimMannschaft() < 0 || spiel.getToreGastMannschaft() < 0 || 
				spiel.getToreHeimMannschaftNachNeunzig() < 0 || spiel.getToreGastMannschaftNachNeunzig() < 0) {
			throw new TorEingabeException("Bitte geben sie nur ganze Zahlen ein, die größer oder gleich 0 sind!");
		}
		
		if(!spiel.getSpielinfo().getSpielinfoTyp().equalsIgnoreCase("---") && 
				(spiel.getToreGastMannschaft() - spiel.getToreHeimMannschaft() == 0)) {
			throw new KeinGewinnerException("Bei Verlängerung und Elfmeterschießen muss es einen Sieger geben. Überprüfen sie ihre Eingabe!");
		}
		
		if(!spiel.getSpielinfo().getSpielinfoTyp().equalsIgnoreCase("---") && 
				!(spiel.getToreGastMannschaftNachNeunzig() - spiel.getToreHeimMannschaftNachNeunzig() == 0)) {
			throw new KeinUnentschiedenBeiVerlaengerungException("Bei Verlängerung und Elfmeterschießen muss "
					+ "das Ergebnis nach 90min Unentschieden lauten!");
		}
		
		if(!spiel.getSpielinfo().getSpielinfoTyp().equalsIgnoreCase("---") && (spiel.getToreGastMannschaft() < spiel.getToreGastMannschaftNachNeunzig() ||
				spiel.getToreHeimMannschaft() < spiel.getToreHeimMannschaftNachNeunzig())) {
			throw new ErgebnisNichtZulaessigException("Das Endergebnis muss mehr Tore haben als das Ergebnis nach 90min. Überprüfen sie ihre Eingabe!");
		}
		
		if(!spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
			if(spiel.getToreHeimMannschaft() == spiel.getToreGastMannschaft()) {
				throw new KOSpielDarfNichtUnentschiedenEndenException("Ein K-o. Spiel darf nicht Unentschieden enden!");
			}
			
		}
		
		// Setzt automatisch das ergebnis nach neunzig Minuten gleich dem Endergebnis wenn das Spiel kein Elfmeterschießen oder Verlängerung hatte
		if(spiel.getSpielinfo().getSpielinfoTyp().equals("---")) {
			spiel.setToreHeimMannschaftNachNeunzig(spiel.getToreHeimMannschaft());
			spiel.setToreGastMannschaftNachNeunzig(spiel.getToreGastMannschaft());
		}
		LOG.info("Das Spiel: {} : {} am {} wurde aktualisiert.", spiel.getHeimMannschaft().getName(), spiel.getGastMannschaft().getName(), 
				spiel.getSpielbeginn());

		spielRepository.save(spiel);
	}
	
	public void newOrEditSpiel(Spiel spiel) throws MannschaftsNameIsEmptyException {
		if(spiel.getGastMannschaft().getName().equals("") || spiel.getHeimMannschaft().getName().equals("")) {
			throw new MannschaftsNameIsEmptyException("Der Mannschaftsname darf nicht leer sein! Bitte legen sie diesen fest");
		}
		LOG.info("Das Spiel: {} : {} am {} wurde aktualisiert.", spiel.getHeimMannschaft().getName(), spiel.getGastMannschaft().getName(), 
				spiel.getSpielbeginn());

		spielRepository.save(spiel);
	}
	
	@Scheduled(fixedDelay=100)
	public void checkTime() {
		LocalDateTime currentDate = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
		for(Spiel s: findeAlleSpiele()) {
			if(s.isTippbar() == false) {	
			} else {
				if(currentDate.isAfter(s.getSpielbeginn()) || currentDate.isEqual(s.getSpielbeginn())) {
					s.setTippbar(false);
				}
			}
		}
	}
	
	public Spiel findeNaechstesSpiel() {
		List<Spiel> sortierteSpiele = findeAlleSpiele();
		Collections.sort(sortierteSpiele);
		for(Spiel spiel : sortierteSpiele) {
			if(spiel.isTippbar()) {
				return spiel;
			}
		}
		return null;
	}

	public void erzeugeSpieleFromExcelFile() {
		//erstellt Spiele anhand der ExcelTabelle
		try {
			ExcelReader excelReader = new ExcelReader(1);
			if (spielRepository.count() == 0) {
				for(int i = 1; i < excelReader.getRows(); i++) {
					spielRepository.save(new Spiel(teamService.findeTeamByString(excelReader.getHeimMannschaft(i)), teamService.findeTeamByString(excelReader.getGastMannschaft(i)), 
							excelReader.getSpielbeginn(i), excelReader.getSpielStatus(i), excelReader.getSpielort(i)));
					LOG.info("Das Spiel {} : {} wurde erzeugt", excelReader.getHeimMannschaft(i), excelReader.getGastMannschaft(i));
				}
			}
		} catch (IOException e) {
			LOG.info("Es wurde die Exception: {} geworfen. Es trat ein Fehler beim einlesen der Excelfiles auf.", e);
		}
	}

	public Spiel findeSpiel(String teamName, String teamName2) {
		for(Spiel spiel : findeAlleSpiele()) {
			if(((teamName.equals(spiel.getHeimMannschaft().getName()) || teamName2.equals(spiel.getHeimMannschaft().getName())) && 
					((teamName.equals(spiel.getGastMannschaft().getName()) || teamName2.equals(spiel.getGastMannschaft().getName()))))) {
				return spiel;
			}
		}
		return null;
	}
}
