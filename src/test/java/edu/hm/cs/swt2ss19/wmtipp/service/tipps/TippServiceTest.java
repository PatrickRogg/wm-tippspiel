package edu.hm.cs.swt2ss19.wmtipp.service.tipps;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import edu.hm.cs.swt2ss18.wmtipp.mvc.GruppenTyp;
import edu.hm.cs.swt2ss18.wmtipp.mvc.SpielinfoTyp;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.ErgebnisNichtZulaessigException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KOSpielDarfNichtUnentschiedenEndenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinGewinnerException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinUnentschiedenBeiVerlaengerungException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.SpielHatAngefangenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.TorEingabeException;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.TurnierStatus;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.Team;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

@RunWith(MockitoJUnitRunner.class)
public class TippServiceTest {
	
		// Testdaten zur Verwendung in den Tests
		private static final Mitspieler ma = new Mitspieler("ma", "password", false, "maAnzeigeName");
		
		private static final LocalDateTime spielbeginn = LocalDateTime.of(2018, 6, 1, 18, 45);
		
		private static final Team heimMannschaftA = new Team("HeimmannschaftA", "WappenHeimmanschaftA", GruppenTyp.GRUPPE_A);
		private static final Team gastMannschaftA = new Team("GastmannschaftA", "WappenGastmanschaftA", GruppenTyp.GRUPPE_A);
		
		private static final Spiel spiel = new Spiel(heimMannschaftA, gastMannschaftA, spielbeginn, TurnierStatus.GRUPPENPHASE, "München");
		
		{
			heimMannschaftA.setId(1L);
			gastMannschaftA.setId(2L);
		}

		// Mocks sind Test-Stümpfe, deren Verhalten man in den Tests definieren
		// kann, d.h. hier werden nicht wirklich Objekte von den entsprechenden
		// Typen abgelegt, sondern programmierbare Dummys.

		@Mock
		MitspielerService mitspielerService;

		@InjectMocks
		TippService tippService = new TippService();
		
		@Mock
		SpielService spielService;
		
		@Mock
		TippRepository tippRepository;
		
		@Mock
		SpielRepository spielRepository;

		@Test
		public void tippWirdAbgespeichert() throws SpielHatAngefangenException, TorEingabeException, KeinGewinnerException, 
			KeinUnentschiedenBeiVerlaengerungException, ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException {
			
			Mockito.when(spielService.findeSpiel(spiel.getId())).thenReturn(spiel);
			Mockito.when(mitspielerService.findeMitspieler(ma.getLogin())).thenReturn(ma);
			
			tippService.legeTippAb("ma", spiel.getId(), 2, 1, 1, 1, SpielinfoTyp.VERLAENGERUNG);

			// Hier werden die Ergebnisse getestet
			Mockito.verify(tippRepository).save(tippService.findeTipp("ma", spiel.getId()));
			
		}
		
		@Test(expected=KeinGewinnerException.class)
		public void tippWirftTorEingabeException() throws SpielHatAngefangenException, TorEingabeException, KeinGewinnerException, 
			KeinUnentschiedenBeiVerlaengerungException, ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException {
			
			Mockito.when(spielService.findeSpiel(spiel.getId())).thenReturn(spiel);	
			
			tippService.legeTippAb("ma", spiel.getId(), 0, 0, 1, 1, SpielinfoTyp.VERLAENGERUNG);
		}
}
