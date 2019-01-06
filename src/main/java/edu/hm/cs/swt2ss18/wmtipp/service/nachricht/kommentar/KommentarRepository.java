package edu.hm.cs.swt2ss18.wmtipp.service.nachricht.kommentar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KommentarRepository extends JpaRepository<Kommentar, Long> {

}
