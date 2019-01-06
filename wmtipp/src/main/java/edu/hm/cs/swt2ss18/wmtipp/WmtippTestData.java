package edu.hm.cs.swt2ss18.wmtipp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import edu.hm.cs.swt2ss18.wmtipp.mvc.SpielinfoTyp;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.AnzeigeNameAlreadyExistsException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.AnzeigeNameIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.ErgebnisNichtZulaessigException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KOSpielDarfNichtUnentschiedenEndenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinGewinnerException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinUnentschiedenBeiVerlaengerungException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginAlreadyExistsException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginIsAdminException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.PasswordIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.SpielHatAngefangenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.TorEingabeException;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.TurnierStatus;
import edu.hm.cs.swt2ss18.wmtipp.service.tabellen.TabellenService;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Diese Klasse enthält Testdaten für manuelle Tests, um das Testen von
 * Änderungen zu beschleunigen.
 * 
 * @author katz.bastian
 */
@Component
@Profile("test")
public class WmtippTestData {
	
	LocalDateTime spielbeginn = LocalDateTime.of(LocalDate.of(2018, 4 , 16), LocalTime.of(18, 30));
	LocalDateTime spielbeginn1 = LocalDateTime.of(LocalDate.of(2018, 4 , 18), LocalTime.of(18, 30));
	LocalDateTime spielbeginn2 = LocalDateTime.of(LocalDate.of(2018, 4 , 20), LocalTime.of(18, 30));
	LocalDateTime spielbeginn3 = LocalDateTime.of(LocalDate.of(2018, 5 , 01), LocalTime.of(13, 07));
	LocalDateTime spielbeginn4 = LocalDateTime.of(LocalDate.of(2019, 4 , 28), LocalTime.of(14, 47));

	@Autowired
	SpielRepository spielRepository;

	@Autowired
	MitspielerService mitspielerService;

	@Autowired
	SpielService spielService;

	@Autowired
	TippService tippService;
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired 
	TabellenService tabellenService;
	
	@Autowired
	TeamService teamService;
	
	String dummyEins = "dummy1";
	String dummyZwei = "dummy2";
	String dummyDrei = "dummy3";
	String dummyVier = "dummy4";
	String dummyFuenf = "dummy5";
	String dummySechs = "dummy6";
	String dummySieben = "dummy7";
	String dummyAcht = "dummy8";
	
	String defaultProfilBild ="default.png";
	
	Random r = new Random();

	@PostConstruct
	public void erzeugeTestDaten() throws LoginIsAdminException, LoginIsEmptyException, 
		AnzeigeNameAlreadyExistsException, LoginAlreadyExistsException, SpielHatAngefangenException, TorEingabeException, 
		KeinGewinnerException, KeinUnentschiedenBeiVerlaengerungException, ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException, 
		PasswordIsEmptyException, AnzeigeNameIsEmptyException {
		erzeugeTestSpieler();
		erzeugeTestTippsGruppenphase();
		erzeugeTestErgebnisseGruppenphase();
	}

	private void erzeugeTestTipps() throws SpielHatAngefangenException, TorEingabeException, KeinGewinnerException, 
	KeinUnentschiedenBeiVerlaengerungException, ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException {
		for(Mitspieler m : mitspielerService.findeNormaleMitspieler()) {
			for(Spiel spiel : spielService.findeAlleSpiele()) {
				tippService.legeTippAb(m.getLogin(), spiel.getId(), r.nextInt(5), r.nextInt(5), 0, 0, SpielinfoTyp.STANDARD);
			}
		}
	}
	
	private void erzeugeTestTippsGruppenphase() throws SpielHatAngefangenException, TorEingabeException, KeinGewinnerException, 
	KeinUnentschiedenBeiVerlaengerungException, ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException {
		for(Mitspieler m : mitspielerService.findeNormaleMitspieler()) {
			for(Spiel spiel : spielService.findeAlleSpiele()) {
				if(spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
					tippService.legeTippAb(m.getLogin(), spiel.getId(), r.nextInt(5), r.nextInt(5), 0, 0, SpielinfoTyp.STANDARD);
				}
			}
		}
	}

	private void erzeugeTestErgebnisse() throws TorEingabeException, KeinGewinnerException, KeinUnentschiedenBeiVerlaengerungException, 
	ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException {
		for (Spiel spiel : spielService.findeAlleSpiele()) {
			if(spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
				spiel.setVorbei(true);
				spiel.setToreHeimMannschaft(r.nextInt(5));
				spiel.setToreGastMannschaft(r.nextInt(5));
				spielService.setzeErgebnis(spiel);
			} else {
				spiel.setVorbei(true);
				spiel.setToreHeimMannschaft(6);
				spiel.setToreGastMannschaft(r.nextInt(5));
				spielService.setzeErgebnis(spiel);
			}
			
			if(tabellenService.tabelleIstFertig(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId())) {
				teamService.updateGruppenTypenFürFertigeGruppe(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId());
				teamService.updateGruppenTypenFürFertigesKOSpiel(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId());
			}
		}
		
	}
	
	private void erzeugeTestErgebnisseGruppenphase() throws TorEingabeException, KeinGewinnerException, KeinUnentschiedenBeiVerlaengerungException, 
	ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException {
		for (Spiel spiel : spielService.findeAlleSpiele()) {
			if(spiel.getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
				spiel.setVorbei(true);
				spiel.setToreHeimMannschaft(r.nextInt(5));
				spiel.setToreGastMannschaft(r.nextInt(5));
				spielService.setzeErgebnis(spiel);
			}

			if(tabellenService.tabelleIstFertig(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId())) {
				teamService.updateGruppenTypenFürFertigeGruppe(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId());
				teamService.updateGruppenTypenFürFertigesKOSpiel(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId());
			}
		}
		
	}

	private void loescheMatches() {
		spielRepository.deleteAll();
	}

	private void erzeugeTestSpieler() throws LoginIsAdminException, LoginIsEmptyException, 
		AnzeigeNameAlreadyExistsException, LoginAlreadyExistsException, PasswordIsEmptyException, AnzeigeNameIsEmptyException {
		// Das {noop} steht für "keinen Hash" -> Praktikum
		mitspielerService.legeAn(new Mitspieler(dummyEins, "1", false, "Tom"));
		mitspielerService.legeAn(new Mitspieler(dummyZwei, "2", false, "Bernd"));
		mitspielerService.legeAn(new Mitspieler(dummyDrei, "3", false, "Gans"));
		mitspielerService.legeAn(new Mitspieler(dummyVier, "4", false, "Rudi"));
		mitspielerService.legeAn(new Mitspieler(dummyFuenf, "5", false, "Elias"));
		mitspielerService.legeAn(new Mitspieler(dummySechs, "6", false, "Matthias"));
		mitspielerService.legeAn(new Mitspieler(dummySieben, "7", false, "Andreas"));
		mitspielerService.legeAn(new Mitspieler(dummyAcht, "8", false, "Mick"));
	}
}
