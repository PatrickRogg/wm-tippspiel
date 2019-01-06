package edu.hm.cs.swt2ss18.wmtipp.service.mitspieler;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Mitspieler implements Comparable<Mitspieler> {

	@Id
	@NotNull
	private String login;
	
	@NotNull
	private String password;
	
	private boolean isAdmin;
	
	@NotNull
	private String anzeigeName;
	
	private String profilBild = "/profilbilder/default.png";
	
	public Mitspieler(@NotNull String login, @NotNull String password, boolean isAdmin, @NotNull String anzeigeName) {
		super();
		this.login = login;
		this.password = password;
		this.isAdmin = isAdmin;
		this.anzeigeName = anzeigeName;
	}

	public Mitspieler() {
		// Do not remove!
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAnzeigeName() {
		return anzeigeName;
	}

	public void setAnzeigeName(String anzeigeName) {
		this.anzeigeName = anzeigeName;
	}

	public String getProfilBild() {
		return profilBild;
	}

	public void setProfilBild(String profilBild) {
		this.profilBild = profilBild;
	}

	@Override
	public int compareTo(Mitspieler compareTo) {
		String compareString = ((Mitspieler)compareTo).getAnzeigeName();
		return this.anzeigeName.compareToIgnoreCase(compareString);
	}
}
