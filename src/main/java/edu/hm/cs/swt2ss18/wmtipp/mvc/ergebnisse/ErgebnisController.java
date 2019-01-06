package edu.hm.cs.swt2ss18.wmtipp.mvc.ergebnisse;

import static edu.hm.cs.swt2ss18.wmtipp.mvc.AuthenticationHelper.isAdmin;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.hm.cs.swt2ss18.wmtipp.mvc.DateHelper;
import edu.hm.cs.swt2ss18.wmtipp.mvc.SpielinfoTyp;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.ErgebnisNichtZulaessigException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KOSpielDarfNichtUnentschiedenEndenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinGewinnerException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinUnentschiedenBeiVerlaengerungException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.MannschaftsNameIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.TorEingabeException;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tabellen.TabellenService;
import edu.hm.cs.swt2ss18.wmtipp.service.teams.TeamService;

@Controller
public class ErgebnisController {

	private static final Logger LOG = LoggerFactory.getLogger(SpielService.class);
	
	@Autowired
	TabellenService tabellenService;
	
	@Autowired 
	TeamService teamService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	MitspielerService mitspielerService;
	
	SpielListe spielListe;
	
	DateHelper dateHelper = new DateHelper();
	
	String previousUrl;


	@GetMapping("/ergebnisse")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String zeigeSpielliste(Model model, Authentication auth) {
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		spielListe = new SpielListe(spielService.findeAlleSpiele());
		Collections.sort(spielListe.getMatches());
		model.addAttribute("administration", isAdmin(auth));
		model.addAttribute("spiele", spielListe.getMatches());
		model.addAttribute("dateHelper", dateHelper);
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		return "ergebnisliste";
	}

	@GetMapping("/ergebnisse/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String showMatch(Model model, Authentication auth, @PathVariable("id") Long id, final HttpServletRequest request) {
		
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		model.addAttribute("administration", isAdmin(auth));
		model.addAttribute("spiel", spielService.findeSpiel(id));
		model.addAttribute("spielinfoValues", SpielinfoTyp.values());
		model.addAttribute("spielinfoStandard", SpielinfoTyp.STANDARD);
		model.addAttribute("turnierStatus", spielService.findeSpiel(id).getTurnierStatus().getTurnierStatus());
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		previousUrl = request.getHeader("referer");
		
		return "ergebnisedit";
	}

	@PostMapping("/ergebnisse/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String aktualisiereSpiel(@Valid @ModelAttribute("spiel") Spiel spiel, BindingResult result, 
			final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Bitte geben sie nur ganze Zahlen ein, "
					+ "die größer oder gleich 0 sind und keine Buchstaben!");
			return "redirect:/ergebnisse/{id}";
		}
		
		try {
			spiel.setVorbei(true);
			spiel.setSpielbeginn(spielService.findeSpiel(spiel.getId()).getSpielbeginn());
			
			spielService.setzeErgebnis(spiel);
			LOG.info("Das Spielergebnis: {} : {} am {} wurde geändert. Es ist {} : {} {} ausgegangen", spiel.getHeimMannschaft().getName(), spiel.getGastMannschaft().getName(), 
					spiel.getSpielbeginn(), spiel.getToreHeimMannschaft(), spiel.getToreGastMannschaft(), spiel.getSpielinfo().getSpielinfoTyp());
			redirectAttributes.addFlashAttribute("successMessage","Beim Spiel " + spiel.getHeimMannschaft().getName() + ":" 
					+ spiel.getGastMannschaft().getName() + " wurde erfolgreich das Ergebnis gesetzt!");
			

			//Für das Vorrücken in der KOPhase
			if(tabellenService.tabelleIstFertig(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId())) {
				teamService.updateGruppenTypenFürFertigeGruppe(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId());
				teamService.updateGruppenTypenFürFertigesKOSpiel(tabellenService.findeTabelleByGruppe(spiel.getHeimMannschaft().getGruppenTyp()).getId());
			}
			
			//Leitet auf tabelle um, wenn der User zuvor auf der Tabellenseite war. Sonst auf /ergebnisse.
			for(int i = 1; i <= tabellenService.findeAlleTabellen().size(); i++) {
				String tabellenUrl = "http://localhost:8000/tabelle/" + i;
				if(tabellenUrl.equals(previousUrl)) {
					return "redirect:" + previousUrl;
				} 
			}
			return "redirect:/ergebnisse";
			
		} catch (TorEingabeException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/ergebnisse/{id}";
		} catch (KeinGewinnerException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/ergebnisse/{id}";
		} catch (KeinUnentschiedenBeiVerlaengerungException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/ergebnisse/{id}";
		} catch (ErgebnisNichtZulaessigException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/ergebnisse/{id}";
		} catch (KOSpielDarfNichtUnentschiedenEndenException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/ergebnisse/{id}";
		}
	}
	
	@PostMapping("/ergebnisse/{id}/tippbar")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String schalteTippbar(@PathVariable("id") Long id, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
		Spiel spiel = spielService.findeSpiel(id);
		spiel.setTippbar(!spiel.isTippbar());
		try {
			spielService.newOrEditSpiel(spiel);
		} catch (MannschaftsNameIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		
		//Leitet auf tabelle um, wenn der User sich auf der Tabellenseite befindet. Sonst auf /ergebnisse.
		for(int i = 1; i <= tabellenService.findeAlleTabellen().size(); i++) {
			String tabellenUrl = "http://localhost:8000/tabelle/" + i;
			if(tabellenUrl.equals(request.getHeader("referer"))) {
				return "redirect:" + request.getHeader("referer");
			} 
		}
		
		return "redirect:/ergebnisse";
	}
}
