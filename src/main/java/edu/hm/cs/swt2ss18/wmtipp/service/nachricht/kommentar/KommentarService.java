package edu.hm.cs.swt2ss18.wmtipp.service.nachricht.kommentar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.NachrichtOhneLeerzeichenException;
import edu.hm.cs.swt2ss18.wmtipp.mvc.exceptions.NachrichtZuLangException;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.Nachricht;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.NachrichtRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.nachricht.NachrichtService;

@Service
@Transactional
public class KommentarService {
	
	@Autowired
	NachrichtService nachrichtService;
	
	@Autowired
	NachrichtRepository nachrichtRepository;
	
	@Autowired
	KommentarRepository kommentarRepository;
	
	public Kommentar findeKommentar (Long id) {
		return kommentarRepository.getOne(id);
	}

	public List<Kommentar> findeAlleKommentare() {
		return kommentarRepository.findAll();
	}
	
	public void neuerKommentar(Kommentar kommentar) throws NachrichtZuLangException, NachrichtOhneLeerzeichenException {
		if(kommentar.getKommentar().length() > 144) {
			throw new NachrichtZuLangException("Der Kommentar darf nur 144 Zeichen lang sein!");
		}
		
		if(countKeinLeerzeichenInKommentar(kommentar)  > 50) {
			throw new NachrichtOhneLeerzeichenException("Um ihre Nachricht für andere Nutzer leserlicher zu gestalten, denken sie über die Nutzung von Leerzeichen nach!");
		}
		kommentarRepository.save(kommentar);
	}
	
	public void loescheKommentar(Kommentar kommentar) {
		kommentarRepository.delete(kommentar);
	}
	
	public void loescheKommentareEinerNachricht(Nachricht nachricht) {
		for(Kommentar kommentar : findeAlleKommentare()) {
			if(kommentar.getNachricht().equals(nachricht)) {
				loescheKommentar(kommentar);
			}
		}
	}
	
	public int countKeinLeerzeichenInKommentar(Kommentar kommentar) {
		int keinLeerzeichenCounter = 0;
		for(int i = 0; i < kommentar.getKommentar().length(); i++) {
			if(kommentar.getKommentar().charAt(i) != ' ') {
				keinLeerzeichenCounter++;
			} else {
				keinLeerzeichenCounter = 0;
			}
		}
		return keinLeerzeichenCounter;
	}
}
