package edu.hm.cs.swt2ss18.wmtipp.mvc.ranking;

import static edu.hm.cs.swt2ss18.wmtipp.mvc.AuthenticationHelper.isAdmin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import edu.hm.cs.swt2ss18.wmtipp.mvc.DateHelper;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.BerechnungsTyp;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Spring MVC Controller für die Übersichtsseite (Mitspieler und Punkte).
 * 
 * @author katz.bastian
 */
@Controller
public class RanglistenController {

	@Autowired
	MitspielerService mitspielerService;

	@Autowired
	TippService tippService;
	
	@Autowired
	SpielService spielService;
	
	DateHelper dateHelper = new DateHelper();
	

	/**
	 * Erstellt das Modell für die Spieler-Rangliste (Ranking-View).
	 * 
	 * @param model
	 *            MVC-Model
	 * @param auth
	 *            Authentifizierung
	 * @return Ranking-View
	 */
	@GetMapping("/")
	public String showRanking(Model model, Authentication auth) {
		
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		model.addAttribute("rangliste", erstelleRangliste());
		model.addAttribute("administration", isAdmin(auth));
		model.addAttribute("punktesystem", tippService);
		model.addAttribute("berechnungsTypValues", BerechnungsTyp.values());
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		return "rangliste";
	}
	
	@PostMapping("/")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeBerechnungsTyp(@ModelAttribute("punktesystem") TippService punktesystem) {
		tippService.setStrategie(punktesystem.getStrategie());
		tippService.getStrategie();
		return "redirect:/";
    }

	/**
	 * Hilfsmethode zum Erstellen der Anzeigeelemente ({@link RanglistenEintrag}s).
	 * 
	 * @return Liste der Anzeigeelemente.
	 */
	protected List<RanglistenEintrag> erstelleRangliste() {
		List<RanglistenEintrag> ranglistenEinträge = new ArrayList<>();
		
		int platzierung = 0;
		int counter = 0;
		
		for (Mitspieler mitspieler : mitspielerService.findeNormaleMitspieler()) {
			RanglistenEintrag eintrag = new RanglistenEintrag();
			eintrag.setAnzeigeName(mitspieler.getAnzeigeName());
			eintrag.setPunkte(tippService.berechneGesamtPunkte(mitspieler.getLogin()));
			ranglistenEinträge.add(eintrag);
		}
		
		// Sortiert die Rangliste und fügt eine Platzierung hinzu
		Collections.sort(ranglistenEinträge);
		for(RanglistenEintrag mitspieler : ranglistenEinträge) {
			for(RanglistenEintrag mitspieler2 : ranglistenEinträge) {
				if(mitspieler.getPunkte() == mitspieler2.getPunkte()) {
					if(mitspieler.getAnzeigeName().equals(mitspieler2.getAnzeigeName())) {
						platzierung = counter + 1;
						mitspieler.setPlatzierung(platzierung);
						break;
					} else {
						mitspieler.setPlatzierung(platzierung);
						break;
					}
					
				}
			}
			counter++;
		}
		return ranglistenEinträge;
	}
}
