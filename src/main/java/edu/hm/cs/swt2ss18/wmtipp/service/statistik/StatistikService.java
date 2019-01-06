package edu.hm.cs.swt2ss18.wmtipp.service.statistik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.hm.cs.swt2ss18.wmtipp.mvc.statistik.StatistikTyp;
import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.MitspielerService;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippRepository;
import edu.hm.cs.swt2ss18.wmtipp.service.tipps.TippService;

@Service
@Transactional
@Component
public class StatistikService {
	
	@Autowired
	MitspielerService mitspielerService;
	
	@Autowired
	TippService tippService;
	
	@Autowired
	TippRepository tippRepo;
	
	@Autowired
	DurchschnittlichePunkte durchschnittlichePunkte;
	
	@Autowired
	AnzahlTipps anzahlTipps;
	
	@Autowired
	AnzahlVerpassterSpiele anzahlVerpassterSpiele;
	
	private StatistikTyp statistikTyp = StatistikTyp.DURCHSCHNITTLICHE_PUNKTE;
	
	@Autowired
	@Qualifier("durchschnittlichePunkte")
	private StatistikImpl statistik;
	
	public StatistikService() {
		statistik = durchschnittlichePunkte;
	}
	
	public void setStatistikTyp(StatistikTyp statistikTyp) {
		this.statistikTyp = statistikTyp;
	}
	
	public StatistikTyp getStatistikTyp() {
		switch (statistikTyp) {
			case DURCHSCHNITTLICHE_PUNKTE:
				this.statistik = durchschnittlichePunkte;
				break;
	
			case ANZAHL_TIPPS:
				this.statistik = anzahlTipps;
				break;
				
			case ANZAHL_VERPASSTER_SPIELE:
				this.statistik = anzahlVerpassterSpiele;
				break;
		}
		return statistikTyp;
	}
	
    public float berechnePunkte(String login) {
    	return statistik.berechnePunkte(login);
    }
}
