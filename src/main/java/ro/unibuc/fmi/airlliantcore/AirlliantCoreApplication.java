package ro.unibuc.fmi.airlliantcore;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;


@EntityScan("ro.unibuc.fmi.airlliantmodel.*")
@SpringBootApplication(scanBasePackages = {"ro.unibuc.fmi"})
public class AirlliantCoreApplication {

    @Value(value = "${spring.jpa.properties.hibernate.jdbc.time_zone}")
    private String timeZone;

    public static void main(String[] args) {
        new SpringApplication(AirlliantCoreApplication.class).run(args);
    }

    @PostConstruct
    private void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(timeZone)));
    }

}