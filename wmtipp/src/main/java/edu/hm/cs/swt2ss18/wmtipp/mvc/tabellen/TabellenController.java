package edu.hm.cs.swt2ss18.wmtipp.mvc.tabellen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.hm.cs.swt2ss18.wmtipp.mvc.DateHelper;
import edu.hm.cs.swt2ss18.wmtipp.mvc.GruppenTyp;
import edu.hm.cs.swt2ss18.wmtipp.mvc.tipps.TippEintrag;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tabellen.TabellenService;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.Tipp;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

@Controller
public class TabellenController {
	@Autowired 
	TeamService teamService;
	
	@Autowired
	TabellenService tabellenService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	TippService tippService;
	
	@Autowired
	MitspielerService mitspielerService;
	
	DateHelper dateHelper = new DateHelper();
	
	@GetMapping("/tabelle/{tabellenId}")
	public String showTabelle(Model model, Authentication auth, @PathVariable("tabellenId") Long tabellenId) {
		boolean isKOPhase = tabellenService.isKOPhase(tabellenId);
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		tabellenService.setGruppenTyp(tabellenService.findeTabelle(tabellenId).getGruppe());
		model.addAttribute("tabelle", tabellenService.erstelleTabelle(tabellenId));
		model.addAttribute("spieleDerGruppe", erstelleSpieleDerGruppe(tabellenId));
		model.addAttribute("tippsDerGruppe", getTippsDerGruppe(auth.getName(), tabellenId));
		model.addAttribute("dateHelper", dateHelper);
		model.addAttribute("administration", auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
		model.addAttribute("gruppeValues", GruppenTyp.values());
		model.addAttribute("gruppe", tabellenService);
		model.addAttribute("kOPhase", isKOPhase);
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		return "tabellenliste";
	}
	
	@PostMapping("/tabelle/{tabellenId}")
    public String changeTabelle(@ModelAttribute("gruppe") TabellenService gruppe) {
		tabellenService.setGruppenTyp(gruppe.getGruppenTyp());
		return "redirect:/tabelle/" + tabellenService.findeTabelleByGruppe(gruppe.getGruppenTyp()).getId();
    }
	
	private List<Spiel> erstelleSpieleDerGruppe(Long tabellenId) {
		List<Spiel> spiele = new ArrayList<>();
		
		for(Spiel spiel : spielService.findeAlleSpiele()) {
			if((spiel.getHeimMannschaft().getGruppenTyp().equals(tabellenService.findeTabelle(tabellenId).getGruppe())) &&
					(spiel.getGastMannschaft().getGruppenTyp().equals(tabellenService.findeTabelle(tabellenId).getGruppe()))) {
				spiele.add(spiel);
			}
		}
		
		return spiele;
	}
	
	private TippEintrag createViewTipp(Spiel spiel, Tipp tipp) {
		TippEintrag tippEintrag = new TippEintrag();
		tippEintrag.setHeimMannschaft(spiel.getHeimMannschaft().getName());
		tippEintrag.setGastMannschaft(spiel.getGastMannschaft().getName());
		tippEintrag.setErgebnis(spiel.getErgebnis());
		tippEintrag.setErgebnisNachNeunzig(spiel.getErgebnisNachNeunzig());
		tippEintrag.setTippbar(spiel.isTippbar());
		tippEintrag.setSpielId(spiel.getId());
		tippEintrag.setVorbei(spiel.isVorbei());
		tippEintrag.setSpielbeginn(dateHelper.dateToString(spiel.getSpielbeginn()));
		if (tipp != null) {
			tippEintrag.setTipp(tipp.getErgebnis());
			tippEintrag.setTippNachNeunzig(tipp.getErgebnisNachNeunzig());
			tippEintrag.setPunkte(tippService.berechnePunkte(tipp.getMitspieler().getLogin(), spiel.getId()));
		}
		return tippEintrag;
	}

	private List<TippEintrag> getTippsDerGruppe(String name, Long tabellenId) {
		List<Spiel> matches = spielService.findeAlleSpiele();
		List<TippEintrag> tipps = new ArrayList<>();

		for (Spiel spiel : matches) {
			if((spiel.getHeimMannschaft().getGruppenTyp().equals(tabellenService.findeTabelle(tabellenId).getGruppe())) &&
					(spiel.getGastMannschaft().getGruppenTyp().equals(tabellenService.findeTabelle(tabellenId).getGruppe()))) {
				TippEintrag viewtipp = createViewTipp(spiel, tippService.findeTipp(name, spiel.getId()));
				tipps.add(viewtipp);
			}

		}
		Collections.sort(tipps);
		return tipps;
	}
}
