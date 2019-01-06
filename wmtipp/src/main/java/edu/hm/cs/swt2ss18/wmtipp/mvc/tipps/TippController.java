package edu.hm.cs.swt2ss18.wmtipp.mvc.tipps;

import static edu.hm.cs.swt2ss18.wmtipp.mvc.AuthenticationHelper.isAdmin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.SpielHatAngefangenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.TorEingabeException;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.Tipp;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

/**
 * Spring MVC Controller für die Tippseiten.
 * 
 * @author katz.bastian
 */
@Controller
public class TippController {

	@Autowired
	SpielService spielService;

	@Autowired
	TippService tippService;

	@Autowired
	MitspielerService mitspielerService;
	
	DateHelper dateHelper = new DateHelper();
	
	@GetMapping("/tipps")
	public String home(Model model, Authentication auth) {
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		model.addAttribute("administration", isAdmin(auth));
		model.addAttribute("tipps", tippService.getTipps(auth.getName()));
		model.addAttribute("dateHelper", dateHelper);
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		return "tipplist";
	}


	@GetMapping("/tipps/{spielId}")
	public String tippEdit(Model model, Authentication auth, @PathVariable("spielId") Long spielId) {
		
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		if(spielService.findeSpiel(spielId).isTippbar() == false) {
			return "redirect:/tipps";
		}
		
		model.addAttribute("administration", isAdmin(auth));
		model.addAttribute("spielinfoValues", SpielinfoTyp.values());
		model.addAttribute("spielinfoStandard", SpielinfoTyp.STANDARD);

		Tipp tipp = tippService.findeTipp(auth.getName(), spielId);
		if (tipp == null) {
			tipp = new Tipp();
		}
		Spiel spiel = spielService.findeSpiel(spielId);
		model.addAttribute("turnierStatus", spiel.getTurnierStatus().getTurnierStatus());
		model.addAttribute("spiel", spiel);
		model.addAttribute("tipp", tipp);
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		return "tippedit";
	}
	
	@PostMapping("/tipps/{spielId}")
	public String createOrUpdateTipp(Authentication auth, @PathVariable("spielId") Long spielId,
			@Valid @ModelAttribute("tipp") Tipp tipp, BindingResult result, 
			final RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Bitte geben sie nur ganze Zahlen ein, "
					+ "die größer oder gleich 0 sind und keine Buchstaben!");
			return "redirect:/tipps/{spielId}";
		}
		
		try {
			tippService.legeTippAb(auth.getName(), spielId, tipp.getToreHeimMannschaft(), tipp.getToreGastMannschaft(), 
					tipp.getToreHeimMannschaftNachNeunzig(), tipp.getToreGastMannschaftNachNeunzig(), tipp.getSpielinfo());
			redirectAttributes.addFlashAttribute("successMessage","Das Spiel " + spielService.findeSpiel(spielId).getHeimMannschaft().getName() + ":" 
					+ spielService.findeSpiel(spielId).getGastMannschaft().getName() + " wurde erfolgreich getippt!");
			return "redirect:/tipps";
		} catch (SpielHatAngefangenException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/tipps";
		} catch (TorEingabeException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/tipps/{spieId}";
		} catch (KeinGewinnerException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/tipps/{spielId}";
		} catch (KeinUnentschiedenBeiVerlaengerungException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/tipps/{spielId}";
		} catch (ErgebnisNichtZulaessigException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/tipps/{spielId}";
		} catch (KOSpielDarfNichtUnentschiedenEndenException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/tipps/{spielId}";
		}

	}
}
