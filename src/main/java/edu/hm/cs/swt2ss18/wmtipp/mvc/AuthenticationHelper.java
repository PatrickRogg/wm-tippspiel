package edu.hm.cs.swt2ss18.wmtipp.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Hilfklasse für Methoden zur Auswertung der Authentifizierung innerhalb der
 * Präsentation.
 * 
 * @author katz.bastian
 */
public class AuthenticationHelper {

	public static boolean isAdmin(Authentication auth) {
		return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
}
