package uk.gov.dvla.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
//@EnableOAuth2Sso
public class BicycleLicenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BicycleLicenceApplication.class, args);
    }
}
