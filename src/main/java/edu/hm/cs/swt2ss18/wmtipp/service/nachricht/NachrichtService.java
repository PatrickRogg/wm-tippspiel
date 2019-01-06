package edu.hm.cs.swt2ss18.wmtipp.service.nachricht;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.NachrichtOhneLeerzeichenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.NachrichtZuLangException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.UeberschriftZuLangException;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.kommentar.KommentarService;

@Service
@Transactional
public class NachrichtService {
	
	@Autowired
	NachrichtRepository nachrichtRepository;
	
	@Autowired
	MitspielerService mitspielerService;
	
	@Autowired
	KommentarService kommentarService;
	
	public Nachricht findeNachricht (Long nachrichtId) {
		return nachrichtRepository.getOne(nachrichtId);
	}

	public List<Nachricht> findeAlleNachrichten() {
		return nachrichtRepository.findAll();
	}
	
	public void neueNachricht(Nachricht nachricht) throws NachrichtZuLangException, NachrichtOhneLeerzeichenException, UeberschriftZuLangException {
		if(nachricht.getUeberschrift().length() > 25) {
			throw new UeberschriftZuLangException("Eine Überschrift darf maximal 25 Zeichen lang sein!");
		}
		
		if(nachricht.getNachricht().length() > 144) {
			throw new NachrichtZuLangException("Die Nachricht darf nur 144 Zeichen lang sein!");
		}
		
		if(countKeinLeerzeichenInNachricht(nachricht)  > 50) {
			throw new NachrichtOhneLeerzeichenException("Um ihre Nachricht für andere Nutzer leserlicher zu gestalten, denken sie über die Nutzung von Leerzeichen nach!");
		}
		
		nachrichtRepository.save(nachricht);
	}
	
	public void loescheNachricht(Nachricht nachricht) {
		nachrichtRepository.delete(nachricht);
	}
	
	public void neuerLike(Mitspieler liker, Nachricht nachricht) {
		findeNachricht(nachricht.getId()).addLiker(liker);
		nachrichtRepository.save(findeNachricht(nachricht.getId()));
	}
	
	public List<Nachricht> findeLetzteFuenfNachrichten() {
		List<Nachricht> letzteFuenfNachrichten = new ArrayList<>();
		List<Nachricht> alleNachrichten = findeAlleNachrichten();
		
		Collections.sort(alleNachrichten);
		int anzahlAllerNachrichten = findeAlleNachrichten().size();
		
		for(int i = 0; i < 5; i++) {
			if(anzahlAllerNachrichten == 0) {
				break;
			} else {
				letzteFuenfNachrichten.add(alleNachrichten.get(i));
				anzahlAllerNachrichten--;
			}
		}
		return letzteFuenfNachrichten;
	}

	public void loescheNachrichtVonEinemMitspieler(Mitspieler mitspieler) {
		for(Nachricht nachricht : findeAlleNachrichtenVonEinemMitspieler(mitspieler))  {
			kommentarService.loescheKommentareEinerNachricht(nachricht);
			loescheNachricht(nachricht);
		}
	}

	public List<Nachricht> findeAlleNachrichtenVonEinemMitspieler(Mitspieler mitspieler) {
		List<Nachricht> alleNachrichten = new ArrayList<>();
		
		for(Nachricht nachricht : findeAlleNachrichten()) {
			if(nachricht.getMitspielerAnzeigeName().equals(mitspieler.getAnzeigeName())) {
				alleNachrichten.add(nachricht);
			}
		}
		return alleNachrichten;
	}
	
	public int countKeinLeerzeichenInNachricht(Nachricht nachricht) {
		int keinLeerzeichenCounter = 0;
		for(int i = 0; i < nachricht.getNachricht().length(); i++) {
			if(nachricht.getNachricht().charAt(i) != ' ') {
				keinLeerzeichenCounter++;
			} else {
				keinLeerzeichenCounter = 0;
			}
		}
		return keinLeerzeichenCounter;
	}
}
