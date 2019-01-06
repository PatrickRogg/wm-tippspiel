package edu.hm.cs.swt2ss18.wmtipp.service.tipps;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.mvc.DateHelper;
import edu.hm.cs.swt2ss18.wmtipp.mvc.SpielinfoTyp;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.ErgebnisNichtZulaessigException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KOSpielDarfNichtUnentschiedenEndenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinGewinnerException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.KeinUnentschiedenBeiVerlaengerungException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.SpielHatAngefangenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.TorEingabeException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.tipps.TippEintrag;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.TurnierStatus;

@Service
@Transactional
public class TippService {

	private static final Logger LOG = LoggerFactory.getLogger(TippService.class);
	
	@Autowired
	TippRepository tippRepository;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	MitspielerService mitspielerService;
	
	BerechnungsTyp strategie = BerechnungsTyp.DREI_PUNKTE_BERECHNUNG;
	
	DateHelper dateHelper = new DateHelper();
	
	private PunkteBerechnung punkteBerechnung;
	
	public TippService(){
		this.punkteBerechnung = DreiPunkteBerechnung.getInstance();
	}
	
	@PreAuthorize("#login == authentication.name")
	public Tipp findeTipp(String login, Long spielId) {
		Spiel match = spielService.findeSpiel(spielId);
		Mitspieler user = mitspielerService.findeMitspieler(login);
		return tippRepository.findOneBySpielAndMitspieler(match, user);
	}
	
	public void loescheAlleTippsEinesMitspielers(Mitspieler mitspieler) {
		for(Tipp tipp : tippRepository.getTippsByMitspieler(mitspielerService.findeMitspieler(mitspieler.getLogin()))) {
			tippRepository.delete(tipp);
		}
	}

	@PreAuthorize("#login == authentication.name")
	public void legeTippAb(String login, Long spielId, int toreHeimMannschaft, int toreGastMannschaft,
			int toreHeimMannschaftNachNeunzig, int toreGastMannschaftNachNeunzig, SpielinfoTyp spielinfo) 
				throws SpielHatAngefangenException, TorEingabeException, KeinGewinnerException, KeinUnentschiedenBeiVerlaengerungException, 
				ErgebnisNichtZulaessigException, KOSpielDarfNichtUnentschiedenEndenException {
		//Verhindert das tippen des Users wenn das Spiel anfängt und er aber davor das Tippfenster geöffnet hat 
		if(spielService.findeSpiel(spielId).isTippbar() == false) {
			throw new SpielHatAngefangenException("Das Spiel hat schon angefangen und kann deshalb nicht mehr getippt werden!");
		}
		
		if(toreHeimMannschaft < 0 || toreGastMannschaft < 0 || toreHeimMannschaftNachNeunzig < 0 || toreGastMannschaftNachNeunzig < 0) {
			throw new TorEingabeException("Bitte geben sie nur ganze Zahlen ein, die größer oder gleich 0 sind!");
		}
		
		if(!spielinfo.getSpielinfoTyp().equalsIgnoreCase("---") && (toreGastMannschaft - toreHeimMannschaft == 0)) {
			throw new KeinGewinnerException("Bei Verlängerung und Elfmeterschießen muss es einen Sieger geben. Überprüfen Sie ihre Eingabe!");
		}
		
		if(!spielinfo.getSpielinfoTyp().equalsIgnoreCase("---") && 
				!(toreGastMannschaftNachNeunzig - toreHeimMannschaftNachNeunzig == 0)) {
			throw new KeinUnentschiedenBeiVerlaengerungException("Bei Verlängerung und Elfmeterschießen muss "
					+ "das Ergebnis nach 90min Unentschieden lauten!");
		}
		
		if(!spielinfo.getSpielinfoTyp().equalsIgnoreCase("---") && (toreGastMannschaft < toreGastMannschaftNachNeunzig ||
				toreHeimMannschaft < toreHeimMannschaftNachNeunzig)) {
			throw new ErgebnisNichtZulaessigException("Das Endergebnis muss mehr Tore haben als das Ergebnis nach 90min. Überprüfen sie ihre Eingabe!");
		}
		
		if(!spielService.findeSpiel(spielId).getTurnierStatus().equals(TurnierStatus.GRUPPENPHASE)) {
			if(toreHeimMannschaft == toreGastMannschaft) {
				throw new KOSpielDarfNichtUnentschiedenEndenException("Ein K-o. Spiel darf nicht Unentschieden enden!");
			}
			
		}
		
		Mitspieler user = mitspielerService.findeMitspieler(login);
		Spiel match = spielService.findeSpiel(spielId);
		Tipp tipp = tippRepository.findOneBySpielAndMitspieler(match, user);
		
		LOG.info("Tipp für das Spiel: {} : {} am {} wird von User: {} abgegeben.", match.getHeimMannschaft().getName(), match.getGastMannschaft().getName(), 
				match.getSpielbeginn(), user.getLogin());
		
		if (tipp == null) {
			tipp = new Tipp();
			tipp.setSpiel(match);
			tipp.setMitspieler(user);
		}
		
		tipp.setToreHeimMannschaft(toreHeimMannschaft);
		tipp.setToreGastMannschaft(toreGastMannschaft);
		tipp.setToreHeimMannschaftNachNeunzig(toreHeimMannschaftNachNeunzig);
		tipp.setToreGastMannschaftNachNeunzig(toreGastMannschaftNachNeunzig);
		tipp.setSpielinfo(spielinfo);
		
		//Wenn das Spiel nach 90min um ist wird das Ergebnis nach 90min automatisch dem Endergebnis angepasst
		if(tipp.getSpielinfo().getSpielinfoTyp().equals("---")) {
			tipp.setToreHeimMannschaftNachNeunzig(tipp.getToreHeimMannschaft());
			tipp.setToreGastMannschaftNachNeunzig(tipp.getToreGastMannschaft());
		}
		tippRepository.save(tipp);
		
		LOG.info("Tipp für das Spiel: {} : {} am {} wurde von User: {} abgegeben. Mit dem Ergebnis {} : {} {}", match.getHeimMannschaft().getName(), 
				match.getGastMannschaft().getName(), match.getSpielbeginn(), user.getLogin(), tipp.getToreHeimMannschaft(), 
				tipp.getToreGastMannschaft(), tipp.getSpielinfo().getSpielinfoTyp());
	}

	public int berechneGesamtPunkte(String login) {
		Mitspieler mitspieler = mitspielerService.findeMitspieler(login);
		List<Tipp> tipps = tippRepository.getTippsByMitspieler(mitspieler);
		int total = 0;
		for (Tipp tipp: tipps) {
			total += berechnePunkte(tipp);
		}
		
		return total;
	}

	public int berechnePunkte(String login, long spielId) {
		Tipp tipp = findeTipp(login, spielId);			
		return berechnePunkte(tipp);
	}
	
	private int berechnePunkte(Tipp tipp) {
		return punkteBerechnung.berechnePunkte(tipp);
	}
	
	public BerechnungsTyp getStrategie(){
		switch(strategie) {
			case EIN_PUNKTE_BERECHNUNG:
				this.punkteBerechnung = EinPunkteBerechnung.getInstance();
				break;
			case DREI_PUNKTE_BERECHNUNG:
				this.punkteBerechnung = DreiPunkteBerechnung.getInstance();
				break;
			case FUENF_PUNKTE_BERECHNUNG:
				this.punkteBerechnung = FuenfPunkteBerechnung.getInstance();
				break;
		}
		return strategie;
	}
	
	public void setStrategie(BerechnungsTyp strategie){
		this.strategie = strategie;
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
			tippEintrag.setTipp(tipp.getErgebnis());
			tippEintrag.setTippNachNeunzig(tipp.getErgebnisNachNeunzig());
			tippEintrag.setPunkte(berechnePunkte(tipp.getMitspieler().getLogin(), spiel.getId()));
		}
		return tippEintrag;
	}

	public List<TippEintrag> getTipps(String name) {
		List<Spiel> matches = spielService.findeAlleSpiele();
		List<TippEintrag> tipps = new ArrayList<>();

		for (Spiel spiel : matches) {
			TippEintrag viewtipp = createViewTipp(spiel, findeTipp(name, spiel.getId()));
			tipps.add(viewtipp);
		}
		Collections.sort(tipps);
		return tipps;
	}
}

