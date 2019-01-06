package edu.hm.cs.swt2ss18.wmtipp.service.spiele;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpielRepository extends JpaRepository<Spiel, Long> {

}
