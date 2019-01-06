package edu.hm.cs.swt2ss18.wmtipp.mvc.mitspieler;

import static edu.hm.cs.swt2ss18.wmtipp.mvc.AuthenticationHelper.isAdmin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.AnzeigeNameAlreadyExistsException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.AnzeigeNameIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginAlreadyExistsException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginIsAdminException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.PasswordIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.tipps.TippEintrag;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.Tipp;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

@Controller
public class MitspielerController {
	
	private static final Logger LOG = LoggerFactory.getLogger(MitspielerController.class);
			
	@Autowired
	MitspielerService mitspielerService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	TippService tippService;
	
	@Autowired
	MitspielerRepository mitspielerRepository;
	
	DateHelper dateHelper = new DateHelper();
		
	@GetMapping("/mitspieler")
	public String home(Model model, Authentication auth) {
		List<Mitspieler> alleMitspieler = mitspielerService.findeNormaleMitspieler();
		
		//löscht den eingeloggten User aus der Liste und zeigt nur seine Mitspieler an
		for(int i = 0; i < alleMitspieler.size(); i++) {
			if(alleMitspieler.get(i).getLogin().equals(auth.getName())) {
				alleMitspieler.remove(i);
			}
		}
		Collections.sort(alleMitspieler);
		
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		model.addAttribute("mitspieler", alleMitspieler);
		model.addAttribute("administration", auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
		return "mitspielerliste";
	}
	
	/*
	 * Mitspielerverwaltung für den Admin
	 */
	
	@GetMapping("/mitspieler/create/admin")
	public String showNeuerMitspielerAdmin(Model model, Authentication auth) {
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		model.addAttribute("administration", auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));		
		model.addAttribute("newMitspieler", new Mitspieler());
		return "mitspielercreateadmin";
	}
	
	@PostMapping("/mitspieler/create/admin")
    public String createNeuerMitspielerAdmin(Model model, @ModelAttribute("newMitspieler") Mitspieler neuerMitspieler, 
    		BindingResult result, final RedirectAttributes redirectAttributes) {	
		try {
			mitspielerService.legeAn(neuerMitspieler);
			redirectAttributes.addFlashAttribute("successMessage", "Der Mitspieler: " + 
					neuerMitspieler.getAnzeigeName() + " wurde erfolgreich angelegt");
			return "redirect:/mitspieler";
		} catch (LoginIsAdminException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/create/admin";
		} catch (LoginIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/create/admin";
		} catch (LoginAlreadyExistsException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/create/admin";
		} catch (AnzeigeNameAlreadyExistsException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/create/admin";
		} catch (PasswordIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/create/admin";
		} catch (AnzeigeNameIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/create/admin";
		}
    }
	
	@GetMapping("/mitspieler/{login}/edit/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String showMitspielerEditAdmin(Model model, Authentication auth, @PathVariable("login") String login) {
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		mitspielerService.findeMitspieler(login).setPassword(mitspielerService.findeMitspieler(login).getPassword().replace("{noop}", ""));
		model.addAttribute("administration", isAdmin(auth));
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		model.addAttribute("editMitspieler", mitspielerService.findeMitspieler(login));
		return "mitspieleredit";
	}
	
	@PostMapping("/mitspieler/{login}/edit/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editMitspielerAdmin(@ModelAttribute("editMitspieler") Mitspieler editMitspieler,
    		final RedirectAttributes redirectAttributes) {
		try {
			mitspielerService.updateMitspieler(editMitspieler);
			redirectAttributes.addFlashAttribute("successMessage", "Der Mitspieler mit dem Login: " + 
					editMitspieler.getLogin() + " wurde erfoglreich bearbeitet");
			return "redirect:/mitspieler";
		} catch (AnzeigeNameAlreadyExistsException e){
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/{login}/edit/admin";
		} catch (PasswordIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/{login}/edit/admin";
		} catch (AnzeigeNameIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/{login}/edit/admin";
		} 
    }
	
	@PostMapping("/mitspieler/{login}/delete/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String deleteMitspielerAdmin(@PathVariable("login") String login, final RedirectAttributes redirectAttributes, Authentication auth, HttpServletRequest request) {
		redirectAttributes.addFlashAttribute("successMessage","Der Mitspieler " + 
				mitspielerService.findeMitspieler(login).getAnzeigeName() + " wurde erfolgreich gelöscht");
		mitspielerService.löscheMitspieler(mitspielerService.findeMitspieler(login));
		
		return "redirect:/mitspieler";
	}
	
	/*
	 * Mitspielerverwaltung für den Mitspieler selbst
	 */
	
	@GetMapping("/create")
	public String showNeuerMitspieler(Model model) {
		model.addAttribute("newMitspieler", new Mitspieler());
		
		return "mitspielercreate";
	}
	
	@PostMapping("/create")
    public String createNeuerMitspieler(@ModelAttribute("newMitspieler") Mitspieler neuerMitspieler, 
    		BindingResult result, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {	
		try {
			mitspielerService.legeAn(neuerMitspieler);
			redirectAttributes.addFlashAttribute("successMessage", "Sie haben sich erfolgreich registriert!");
			
			// User wird automatisch eingelogged nachdem er sich registriert hat
			try {
				request.login(neuerMitspieler.getLogin(), neuerMitspieler.getPassword().replace("{noop}", ""));
				} catch(ServletException e) {
					LOG.info("Es wurde die Exception: {} geworfen. Es trat ein Fehler beim automatischen einloggen des Users nach Registierung auf.", e);
					return "redirect:/";
				}
			return "redirect:/";
		} catch (LoginIsAdminException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/create";
		} catch (LoginIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/create";
		} catch (LoginAlreadyExistsException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/create";
		} catch (AnzeigeNameAlreadyExistsException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/create";
		} catch (PasswordIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/create";
		} catch (AnzeigeNameIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/create";
		}
    }
	
	@GetMapping("/mitspieler/{anzeigeName}/edit")
	public String zeigeMitspielerBearbeiten(Model model, Authentication auth, @PathVariable("anzeigeName") String anzeigeName) {
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		// nur admin und der Mitspieler können den eigenen account bearbeiten
		boolean istEigenerAccount = false;
		
		if(auth.getName().equals(mitspielerService.findeMitspielerByAnzeigeName(anzeigeName).getLogin())) {
			istEigenerAccount = true;
		}
		
		// Ändert das Passwort wieder in Klartext
		mitspielerService.findeMitspielerByAnzeigeName(anzeigeName).setPassword(
				mitspielerService.findeMitspielerByAnzeigeName(anzeigeName).getPassword().replace("{noop}", ""));
		
		model.addAttribute("eigenerAccount", istEigenerAccount);
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
		model.addAttribute("editMitspieler", mitspielerService.findeMitspielerByAnzeigeName(anzeigeName));
		
		return "mitspieleredit";
	}
	
	@PostMapping("/mitspieler/{anzeigeName}/edit")
    public String bearbeiteMitspieler(@ModelAttribute("editMitspieler") Mitspieler editMitspieler,
    		final RedirectAttributes redirectAttributes) {
		try {
			mitspielerService.updateMitspieler(editMitspieler);
			redirectAttributes.addFlashAttribute("successMessage", "Der Mitspieler mit dem Login: " + 
					editMitspieler.getLogin() + " wurde erfoglreich bearbeitet");
			return "redirect:/mitspieler";
		} catch (AnzeigeNameAlreadyExistsException e){
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/{anzeigeName}/edit";
		} catch (PasswordIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/{anzeigeName}/edit";
		} catch (AnzeigeNameIsEmptyException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/mitspieler/{anzeigeName}/edit";
		}
    }
	
	@PostMapping("/mitspieler/{anzeigeName}/delete")
	public String loescheMitspieler(@PathVariable("anzeigeName") String anzeigeName, final RedirectAttributes redirectAttributes, Authentication auth, HttpServletRequest request) {
		redirectAttributes.addFlashAttribute("successMessage","Der Mitspieler " + anzeigeName + " wurde erfolgreich gelöscht");
		
		mitspielerService.löscheMitspieler(mitspielerService.findeMitspielerByAnzeigeName(anzeigeName));
		
		try {
			request.logout();
			return "redirect:/login.html";
		} catch (ServletException e) {
			LOG.info("Es wurde die Exception: {} geworfen. Es trat ein Fehler beim automatischen auslogges des Users nach löschen des Accounts auf.", e);
			return "redirect:/login.html";
		}
	}
	
	@GetMapping("/profil")
	public String zeigeMitspielerEigenesProfil(Model model, Authentication auth, final RedirectAttributes redirectAttributes) {
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		model.addAttribute("administration",  auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
		model.addAttribute("dateHelper", dateHelper);
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
	  
		return "profilanzeigen";
	}
	
	@PostMapping("/mitspieler/{anzeigeName}/edit/profilbild")
	public String aendereProfilBild(Model model, Authentication auth, @ModelAttribute("aktuellerMitspieler") Mitspieler aktuellerMitspieler,  
			@PathVariable("anzeigeName") String anzeigeName, final RedirectAttributes redirectAttributes) {
		
		mitspielerService.speichereProfilBildMitspieler(mitspielerService.findeMitspielerByAnzeigeName(anzeigeName), aktuellerMitspieler.getProfilBild());
		
		return "redirect:/profil";
	}
	
	@GetMapping("/mitspieler/{anzeigeName}")
	public String zeigeMitspielerTipps(Model model, Authentication auth, @PathVariable("anzeigeName") String anzeigeName, 
			final RedirectAttributes redirectAttributes) {
		// für den Countdown
		if(spielService.findeNaechstesSpiel() == null) {
			model.addAttribute("naechstesSpielSpielbeginn", null);
			model.addAttribute("naechstesSpiel", null);
		} else {
			model.addAttribute("naechstesSpielSpielbeginn", dateHelper.dateToStringJavascript(spielService.findeNaechstesSpiel().getSpielbeginn()));
			model.addAttribute("naechstesSpiel", spielService.findeNaechstesSpiel());
		}
		
		// Wenn der User auf die eigenen Tipps sehen will und er es in der Adresszeile eingibt, dann wird er auf seine eigenen Tipps umgeleitet
		if(mitspielerService.findeMitspielerByAnzeigeName(anzeigeName).getAnzeigeName() == "" || 
				mitspielerService.findeMitspielerByAnzeigeName(anzeigeName).equals(mitspielerService.findeMitspieler(auth.getName()))) {
			return "redirect:/tipps";
		} else {
			model.addAttribute("tipps", getTipps(mitspielerService.findeMitspielerByAnzeigeName(anzeigeName).getLogin()));
		}
		
		model.addAttribute("administration",  auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
		model.addAttribute("dateHelper", dateHelper);
		model.addAttribute("auth", auth);
		model.addAttribute("aktuellerMitspieler", mitspielerService.findeMitspieler(auth.getName()));
	  
		return "mitspieleranzeigen";
	}
	
	public TippEintrag createViewTipp(Spiel spiel, Tipp tipp) {
		TippEintrag tippEintrag = new TippEintrag();
		tippEintrag.setHeimMannschaft(spiel.getHeimMannschaft().getName());
		tippEintrag.setGastMannschaft(spiel.getGastMannschaft().getName());
		tippEintrag.setErgebnis(spiel.getErgebnis());
		tippEintrag.setErgebnisNachNeunzig(spiel.getErgebnisNachNeunzig());
		tippEintrag.setTippbar(spiel.isTippbar());
		tippEintrag.setSpielId(spiel.getId());
		tippEintrag.setVorbei(spiel.isVorbei());
		tippEintrag.setSpielbeginn(dateHelper.dateToString(spiel.getSpielbeginn()));
		tippEintrag.setTurnierStatus(spiel.getTurnierStatus().getTurnierStatus());
		if (tipp != null) {
			if(spiel.isVorbei()) {
				tippEintrag.setTipp(tipp.getErgebnis());
				tippEintrag.setTippNachNeunzig(tipp.getErgebnisNachNeunzig());
			}
			
			tippEintrag.setPunkte(tippService.berechnePunkte(tipp.getMitspieler().getLogin(), spiel.getId()));
		}
		return tippEintrag;
	}

	public List<TippEintrag> getTipps(String name) {
		List<Spiel> matches = spielService.findeAlleSpiele();
		List<TippEintrag> tipps = new ArrayList<>();

		for (Spiel spiel : matches) {
			TippEintrag viewtipp = createViewTipp(spiel, tippService.findeTipp(name, spiel.getId()));
			tipps.add(viewtipp);
		}
		Collections.sort(tipps);
		return tipps;
	}
}
