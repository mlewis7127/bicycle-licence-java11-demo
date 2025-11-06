package uk.gov.dvla.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableOAuth2Sso
public class BicycleLicenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BicycleLicenceApplication.class, args);
    }
}
