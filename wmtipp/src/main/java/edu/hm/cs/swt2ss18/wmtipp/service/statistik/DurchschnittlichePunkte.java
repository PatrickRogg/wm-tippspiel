package edu.hm.cs.swt2ss18.wmtipp.service.statistik;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.SpielRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

@Component(value="durchschnittlichePunkte")
public class DurchschnittlichePunkte implements StatistikImpl {
	
	@Autowired
	MitspielerService mitspielerService;
	
	@Autowired
	TippService tippService;
	
	@Autowired
	SpielRepository spielRepository;
	
	@Autowired
	TippRepository tippRepository;
	
	@Override
	public float berechnePunkte(String login) {
		List<Spiel> spiele = spielRepository.findAll();
		tippRepository.getTippsByMitspieler(mitspielerService.findeMitspieler(login));
		
		float punkte = (float)tippService.berechneGesamtPunkte(login) / spiele.size();
		
		return punkte;
	}
}
