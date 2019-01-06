package edu.hm.cs.swt2ss18.wmtipp.service.tipps;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.hm.cs.swt2ss18.wmtipp.service.mitspieler.Mitspieler;
import edu.hm.cs.swt2ss18.wmtipp.service.spiele.Spiel;

@Repository
public interface TippRepository extends JpaRepository<Tipp, Long>{

	Tipp findOneBySpielAndMitspieler(Spiel match, Mitspieler user);

	List<Tipp> getTippsByMitspieler(Mitspieler user);

}
