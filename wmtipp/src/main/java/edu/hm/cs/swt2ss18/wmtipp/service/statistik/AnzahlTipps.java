package edu.hm.cs.swt2ss18.wmtipp.service.statistik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippRepository;

@Component(value="anzahTipps")
public class AnzahlTipps implements StatistikImpl {
	
	@Autowired
	MitspielerService mitspielerService;
	
	@Autowired
	TippRepository tippRepo;
	
	@Override
	public float berechnePunkte(String login) {
		Mitspieler mitspieler = mitspielerService.findeMitspieler(login);
		float punkte = tippRepo.getTippsByMitspieler(mitspieler).size();
		
		return punkte;
	}
}
