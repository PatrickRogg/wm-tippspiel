package edu.hm.cs.swt2ss18.wmtipp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot Webanwendung.
 * 
 * @author katz.bastian
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class WmtippApplication {

	public static void main(String[] args) {
		SpringApplication.run(WmtippApplication.class, args);
	}
}

