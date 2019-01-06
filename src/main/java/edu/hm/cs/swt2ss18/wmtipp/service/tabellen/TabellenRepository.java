package edu.hm.cs.swt2ss18.wmtipp.service.tabellen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TabellenRepository extends JpaRepository<Tabelle, Long> {

}
