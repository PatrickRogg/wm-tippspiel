package edu.hm.cs.swt2ss18.wmtipp.service.statistik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.Tipp;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippRepository;

@Component(value="anzahlVerpassterSpiele")
public class AnzahlVerpassterSpiele implements StatistikImpl {
	
	@Autowired
	SpielRepository spielRepository;
	
	@Autowired
	TippRepository tippRepository;
	
	@Autowired
	MitspielerService mitspielerService;
	
	@Autowired
	SpielService spielService;
	
	@Override
	public float berechnePunkte(String login) {
		int tippCounter = 0;
		int spielCounter = 0;
		Mitspieler mitspieler = mitspielerService.findeMitspieler(login);
		
		for(Spiel s : spielService.findeAlleSpiele()) {
			if(!s.isTippbar()) {
				spielCounter++;
			}
		}
		
		for(Tipp t : tippRepository.getTippsByMitspieler(mitspieler)) {
			if(!t.getErgebnis().isEmpty()) {
				tippCounter++;
			}
		}
		
		if(tippCounter > spielCounter) {
			return 0;
		} else {
			return (float) spielCounter - tippCounter;
		}
	}
}