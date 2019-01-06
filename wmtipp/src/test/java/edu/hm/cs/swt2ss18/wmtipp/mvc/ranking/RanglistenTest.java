package edu.hm.cs.swt2ss18.wmtipp.mvc.ranking;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import edu.hm.cs.swt2ss18.wmtipp.mvc.ranking.RanglistenController;
import edu.hm.cs.swt2ss18.wmtipp.mvc.ranking.RanglistenEintrag;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Test für den RanglistenController, überprüft die Sortierung.
 * 
 * @author katz.bastian
 */
@RunWith(MockitoJUnitRunner.class)
public class RanglistenTest {

	// Testdaten zur Verwendung in den Tests
	private static final Mitspieler ma = new Mitspieler("ma", "password", false, "maAnzeigeName");
	private static final Mitspieler mb = new Mitspieler("mb", "password", false, "mbAnzeigeName");

	// Mocks sind Test-Stümpfe, deren Verhalten man in den Tests definieren
	// kann, d.h. hier werden nicht wirklich Objekte von den entsprechenden
	// Typen abgelegt, sondern programmierbare Dummys.
	@Mock
	MitspielerService mitspielerService;

	@Mock
	TippService tippService;

	// Dem Testobjekt werden Mocks anstelle von echten Komponenten injiziert
	@InjectMocks
	RanglistenController ranglistenController = new RanglistenController();

	@Test
	public void sortedRangliste() {

		// Mit "when" / "thenReturn" wird partielles Verhalten für Stümpfe
		// festgelegt
		Mockito.when(mitspielerService.findeNormaleMitspieler()).thenReturn(Arrays.asList(ma, mb));
		Mockito.when(tippService.berechneGesamtPunkte(ma.getLogin())).thenReturn(12);
		Mockito.when(tippService.berechneGesamtPunkte(mb.getLogin())).thenReturn(24);

		// Hier wird das Modul aufgerufen
		List<RanglistenEintrag> rangliste = ranglistenController.erstelleRangliste();

		// Hier werden die Ergebnisse getestet
		Assert.assertEquals(mb.getAnzeigeName(), rangliste.get(0).getAnzeigeName());
		Assert.assertEquals(ma.getAnzeigeName(), rangliste.get(1).getAnzeigeName());
		
		Assert.assertEquals(1, rangliste.get(0).getPlatzierung());
		Assert.assertEquals(2, rangliste.get(1).getPlatzierung());
	}
	
	@Test
	public void sortedRanglisteGleichePunkte() {

		// Mit "when" / "thenReturn" wird partielles Verhalten für Stümpfe
		// festgelegt
		Mockito.when(mitspielerService.findeNormaleMitspieler()).thenReturn(Arrays.asList(ma, mb));
		Mockito.when(tippService.berechneGesamtPunkte(ma.getLogin())).thenReturn(12);
		Mockito.when(tippService.berechneGesamtPunkte(mb.getLogin())).thenReturn(12);

		// Hier wird das Modul aufgerufen
		List<RanglistenEintrag> rangliste = ranglistenController.erstelleRangliste();

		// Hier werden die Ergebnisse getestet
		Assert.assertEquals(ma.getAnzeigeName(), rangliste.get(0).getAnzeigeName());
		Assert.assertEquals(mb.getAnzeigeName(), rangliste.get(1).getAnzeigeName());
		
		Assert.assertEquals(1, rangliste.get(0).getPlatzierung());
		Assert.assertEquals(1, rangliste.get(1).getPlatzierung());
	}
	
	@Test
	public void sortedRanglisteEinMitspieler() {

		// Mit "when" / "thenReturn" wird partielles Verhalten für Stümpfe
		// festgelegt
		Mockito.when(mitspielerService.findeNormaleMitspieler()).thenReturn(Arrays.asList(ma));
		Mockito.when(tippService.berechneGesamtPunkte(ma.getLogin())).thenReturn(12);

		// Hier wird das Modul aufgerufen
		List<RanglistenEintrag> rangliste = ranglistenController.erstelleRangliste();

		// Hier werden die Ergebnisse getestet
		Assert.assertEquals(ma.getAnzeigeName(), rangliste.get(0).getAnzeigeName());
		
		// Platzierung wird verglichen
		Assert.assertEquals(1, rangliste.get(0).getPlatzierung());
	}
	
	@Test
	public void sortedRanglisteKeinMitspieler() {

		// Hier wird das Modul aufgerufen
		List<RanglistenEintrag> rangliste = ranglistenController.erstelleRangliste();

		// Hier werden die Ergebnisse getestet
		Assert.assertEquals(true , rangliste.isEmpty());
		
		// Platzierung wird verglichen
	}
}