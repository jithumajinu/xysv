package io.crm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.lang.management.ManagementFactory;

import java.security.Security;
import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
@EntityScan(basePackageClasses = {
        CrmAppApplication.class,
        Jsr310JpaConverters.class
})
public class CrmAppApplication  extends AbstractApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    public static void main(final String[] args) {

        Security.setProperty("networkaddress.cache.ttl", "5");
//        log.info("==========JVM Input Arguments==========");
//        ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(log::info);
//        log.info("=======================================");
        System.out.println("==========JVM Input Arguments==========");
        ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(System.out::println);  // forEach(log::info);
        System.out.println("=======================================");

        systemSettingLog();
        SpringApplication.run(CrmAppApplication.class, args);

    }

}
