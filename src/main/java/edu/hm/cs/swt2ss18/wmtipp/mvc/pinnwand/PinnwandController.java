package edu.hm.cs.swt2ss18.wmtipp.mvc.pinnwand;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.hm.cs.swt2ss18.wmtipp.mvc.DateHelper;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.NachrichtOhneLeerzeichenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.NachrichtZuLangException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.UeberschriftZuLangException;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.Nachricht;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.NachrichtService;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.kommentar.Kommentar;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.kommentar.KommentarService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;

@Controller
public class PinnwandController {
	
	private static final Logger LOG = LoggerFactory.getLogger(PinnwandController.class);

	@Autowired
	NachrichtService nachrichtService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	MitspielerService mitspielerService;
	
	@Autowired
	KommentarService kommentarService;
	
	DateHelper dateHelper = new DateHelper();

	@GetMapping("/pinnwand")
	public String showPinnwand(Model model, Authentication auth) {
		
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		model.addAttribute("administration", auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
		model.addAttribute("neueNachricht", new Nachricht());
		model.addAttribute("neuerKommentar", new Kommentar());
		model.addAttribute("nachrichten", nachrichtService.findeLetzteFuenfNachrichten());
		model.addAttribute("kommentare", kommentarService.findeAlleKommentare());
		model.addAttribute("dateHelper", dateHelper);
		model.addAttribute("auth", auth);
		model.addAttribute("mitspielerService", mitspielerService);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		return "pinnwand";
	}
	
	@GetMapping("/pinnwand/alleNachrichten")
	public String showPinnwandAlleNachrichten(Model model, Authentication auth) {
		
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		List<Nachricht> alleNachrichten = nachrichtService.findeAlleNachrichten();
		Collections.sort(alleNachrichten);
		
		model.addAttribute("administration", auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
		model.addAttribute("neueNachricht", new Nachricht());
		model.addAttribute("neuerKommentar", new Kommentar());
		model.addAttribute("nachrichten", alleNachrichten);
		model.addAttribute("kommentare", kommentarService.findeAlleKommentare());
		model.addAttribute("dateHelper", dateHelper);
		model.addAttribute("auth", auth);
		model.addAttribute("mitspielerService", mitspielerService);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		
		return "pinnwandallenachrichten";
	}
	
	@PostMapping("/pinnwand")
    public String createNewNachricht(Model model, @ModelAttribute("neueNachricht") Nachricht nachricht, 
    		BindingResult result, final RedirectAttributes redirectAttributes, Authentication auth) {	
			LocalDateTime currentDate = LocalDateTime.now();
			
			nachricht.setDatum(currentDate);
			nachricht.setMitspielerAnzeigeName(mitspielerService.findeMitspieler(auth.getName()).getAnzeigeName());

			try {
				nachrichtService.neueNachricht(nachricht);
			} catch (NachrichtZuLangException e) {
				redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:/pinnwand";
			} catch (NachrichtOhneLeerzeichenException e) {
				redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:/pinnwand";
			} catch (UeberschriftZuLangException e) {
				redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:/pinnwand";
			}
			
			LOG.info("Nachricht erstellt von: {} mit der Überschrift: {} Nachricht:{} am {}", nachricht.getMitspielerAnzeigeName(), 
					nachricht.getUeberschrift(), nachricht.getNachricht(), nachricht.getDatum());

			
			return "redirect:/pinnwand";
    }
	
	@PostMapping("/pinnwand/{nachrichtId}")
    public String createNewKommentar(Model model, @ModelAttribute("neuerKommentar") Kommentar kommentar, @PathVariable("nachrichtId") Long nachrichtId,
    		BindingResult result, final RedirectAttributes redirectAttributes, Authentication auth) {	
			LocalDateTime currentDate = LocalDateTime.now();
			
			kommentar.setNachricht(nachrichtService.findeNachricht(nachrichtId));
			kommentar.setDatum(currentDate);
			kommentar.setMitspielerAnzeigeName(mitspielerService.findeMitspieler(auth.getName()).getAnzeigeName());

			try {
				kommentarService.neuerKommentar(kommentar);
			} catch (NachrichtZuLangException e) {
				redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:/pinnwand";
			} catch (NachrichtOhneLeerzeichenException e) {
				redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:/pinnwand";
			}
			
			LOG.info("Neuer Kommentar für die Nachricht: {} mit dem Kommentar: {}", 
					nachrichtService.findeNachricht(kommentar.getNachricht().getId()).getNachricht(), 
					kommentarService.findeKommentar(kommentar.getId()).getKommentar());
			
			return "redirect:/pinnwand";
    }

	@PostMapping("/pinnwand/{nachrichtId}/like")
    public String newLike(@PathVariable("nachrichtId") Long nachrichtId, final RedirectAttributes redirectAttributes, Authentication auth) {	
			nachrichtService.neuerLike(mitspielerService.findeMitspieler(auth.getName()), nachrichtService.findeNachricht(nachrichtId));
			
			LOG.info("Neuer Like von: {} für die Nachricht: {} mit: {} Likes", nachrichtService.findeNachricht(nachrichtId).getMitspielerAnzeigeName(), 
					nachrichtService.findeNachricht(nachrichtId).getNachricht(), nachrichtService.findeNachricht(nachrichtId).getLikes());
			return "redirect:/pinnwand";
    }
}
