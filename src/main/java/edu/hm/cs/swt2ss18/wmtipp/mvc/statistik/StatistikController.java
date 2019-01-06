package edu.hm.cs.swt2ss18.wmtipp.mvc.statistik;

import static edu.hm.cs.swt2ss18.wmtipp.mvc.AuthenticationHelper.isAdmin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import edu.hm.cs.swt2ss18.wmtipp.service.statistik.StatistikService;

@Controller
public class StatistikController {
	
	@Autowired
	MitspielerService mitspielerService;

	@Autowired
	StatistikService statistikService;
	
	@Autowired
	SpielService spielService;
	
	DateHelper dateHelper = new DateHelper();
	
	@GetMapping("/statistik")
	public String showStatistik(Model model, Authentication auth) {
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		model.addAttribute("statistik", erstelleStatistik());
		model.addAttribute("administration", isAdmin(auth));
		model.addAttribute("statistikValues", StatistikTyp.values());
		model.addAttribute("statistikAuswahl", statistikService);
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		return "statistikliste";
	}
	
	@PostMapping("/statistik")
    public String statistikChange(@ModelAttribute("statistikAuswahl") StatistikService statistikAuswahl) {
		statistikService.setStatistikTyp(statistikAuswahl.getStatistikTyp());
		statistikService.getStatistikTyp();
		return "redirect:/statistik";
    }
	
	private List<StatistikEintrag> erstelleStatistik() {
		List<StatistikEintrag> statistikEinträge = new ArrayList<>();
		
		int platzierung = 0;
		int counter = 0;
		
		for (Mitspieler mitspieler : mitspielerService.findeNormaleMitspieler()) {
			StatistikEintrag eintrag = new StatistikEintrag();
			eintrag.setAnzeigeName(mitspieler.getAnzeigeName());
			eintrag.setPunkte(statistikService.berechnePunkte(mitspieler.getLogin()));
			statistikEinträge.add(eintrag);
		}
		
		// Sortiert die Statistik und fügt eine Platzierung hinzu
		Collections.sort(statistikEinträge);
		for(StatistikEintrag mitspieler : statistikEinträge) {
			for(StatistikEintrag mitspieler2 : statistikEinträge) {
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
		return statistikEinträge;
	}
}
