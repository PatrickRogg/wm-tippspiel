package edu.hm.cs.swt2ss18.wmtipp.service.mitspieler;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.AnzeigeNameAlreadyExistsException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.AnzeigeNameIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginAlreadyExistsException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginIsAdminException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.LoginIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.PasswordIsEmptyException;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.NachrichtService;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.kommentar.KommentarService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

@Service
@Transactional
public class MitspielerService implements UserDetailsService {

	private static final Collection<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
	private static final Collection<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");
	
	private static final Logger LOG = LoggerFactory.getLogger(MitspielerService.class);

	@Value("${wmtipp.admin.login}")
	String adminLogin;

	@Value("${wmtipp.admin.password}")
	String adminPassword;

	@Autowired
	private MitspielerRepository mitspielerRepository;
	
	@Autowired
	NachrichtService nachrichtService;
	
	@Autowired
	KommentarService kommentarService;
	
	@Autowired
	TippService tippService;
	/**
	 * Bootstrapping: Überprüft, dass der in der Konfiguration angegebene
	 * Administrator-Account existiert und legt ihn ggf. an.
	 * @throws IOException 
	 */
	@PostConstruct
	public void checkAdminAccount() {
		if (!mitspielerRepository.findById(adminLogin).isPresent()) {		
			// Passwörter müssen Hashverfahren benennen. Wir hashen nicht (noop)
			mitspielerRepository.save(new Mitspieler(adminLogin, "{noop}" + adminPassword, true, "admin"));
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Mitspieler> findeMitspieler = mitspielerRepository.findById(username);
		if (findeMitspieler.isPresent()) {
			Mitspieler user = findeMitspieler.get();
			return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
					user.isAdmin() ? ADMIN_ROLES : USER_ROLES);
		} else {
			throw new UsernameNotFoundException("");
		}
	}

	public List<Mitspieler> findeNormaleMitspieler() {
		return mitspielerRepository.findByIsAdmin(false);
	}

	public void legeAn(Mitspieler neuerMitspieler) throws LoginIsAdminException, LoginIsEmptyException, 
		AnzeigeNameAlreadyExistsException, LoginAlreadyExistsException, PasswordIsEmptyException, AnzeigeNameIsEmptyException {
		if(neuerMitspieler.getLogin().equals("admin")) {
			throw new LoginIsAdminException("Der Loginname darf nicht admin lauten!");
		}
		
		if(neuerMitspieler.getLogin().equals("")) {
			throw new LoginIsEmptyException("Der Loginname darf leer sein!");
		}
		
		if(neuerMitspieler.getPassword().equals("")) {
			throw new PasswordIsEmptyException("Das Passwort darf nicht leer sein!");
		}
		
		if(neuerMitspieler.getAnzeigeName().equals("")) {
			throw new AnzeigeNameIsEmptyException("Das Anzeigename darf nicht leer sein!");
		}
		
		for(Mitspieler m : findeNormaleMitspieler()) {
			if(neuerMitspieler.getLogin().equals(m.getLogin())) {
				throw new LoginAlreadyExistsException("Der Loginname: " + 
						neuerMitspieler.getLogin() + " existiert schon!");
			}
			
			if(neuerMitspieler.getAnzeigeName().equals(m.getAnzeigeName())) {
				throw new AnzeigeNameAlreadyExistsException("Der Anzeigename: " + 
						neuerMitspieler.getAnzeigeName() + " existiert schon! Fügen sie evtl. eine Zahl an.");
			}
		}
		
		neuerMitspieler.setPassword("{noop}" + neuerMitspieler.getPassword());
		LOG.info("Mitspieler {} wird angelegt.", neuerMitspieler.getLogin());
		mitspielerRepository.save(neuerMitspieler);
	}
	
	public void updateMitspieler(Mitspieler editUser) throws AnzeigeNameAlreadyExistsException, PasswordIsEmptyException, 
		AnzeigeNameIsEmptyException {
		for(Mitspieler m : findeNormaleMitspieler()) {
			if(editUser.getAnzeigeName().equals(m.getAnzeigeName()) && !editUser.getLogin().equals(m.getLogin())) {
				throw new AnzeigeNameAlreadyExistsException("Der Anzeigename: " + 
						editUser.getAnzeigeName() + " existiert schon! Fügen sie evtl. eine Zahl an.");
			}
		}
		
		if(editUser.getPassword().equals("")) {
			throw new PasswordIsEmptyException("Das Passwort darf nicht leer sein!");
		}
		
		if(editUser.getAnzeigeName().equals("")) {
			throw new AnzeigeNameIsEmptyException("Das Anzeigename darf nicht leer sein!");
		}
		
		findeMitspieler(editUser.getLogin()).setAnzeigeName(editUser.getAnzeigeName());
		findeMitspieler(editUser.getLogin()).setPassword("{noop}" + editUser.getPassword());
		
		LOG.info("Mitspieler {} wird bearbeitet.", editUser.getLogin());
		mitspielerRepository.save(findeMitspieler(editUser.getLogin()));
	}
	
	public void speichereProfilBildMitspieler(Mitspieler aktuellerMitspieler, String profilBild) {
		aktuellerMitspieler.setProfilBild(profilBild);
		mitspielerRepository.save(aktuellerMitspieler);
		LOG.info("Für den Mitspieler {} wurde das Profilbild {} geändert.", findeMitspieler(aktuellerMitspieler.getLogin()).getLogin(), profilBild);

	}
	
	@PreAuthorize("#name == authentication.name or hasRole('ROLE_ADMIN')")
	public void löscheMitspieler(Mitspieler deleteUser){
		tippService.loescheAlleTippsEinesMitspielers(deleteUser);
		
		LOG.info("Mitspieler {} wurde gelöscht.", deleteUser.getLogin());
		mitspielerRepository.deleteById(deleteUser.getLogin());
	}
	
	@PreAuthorize("#name == authentication.name or hasRole('ROLE_ADMIN')")
	public Mitspieler findeMitspieler(String name) {
		return mitspielerRepository.getOne(name);
	}
	
	public Mitspieler findeMitspielerByAnzeigeName(String anzeigeName) {
		for(Mitspieler m : mitspielerRepository.findAll()) {
			if(m.getAnzeigeName().equals(anzeigeName)) {
				return m;
			}
		}
		return null;
	}
}
