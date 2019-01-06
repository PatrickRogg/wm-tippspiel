package edu.hm.cs.swt2ss18.wmtipp.service.nachricht;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NachrichtRepository extends JpaRepository<Nachricht, Long> {

}
