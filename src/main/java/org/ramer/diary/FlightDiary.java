package org.ramer.diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by RAMER on 5/17/2017.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class FlightDiary extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(FlightDiary.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FlightDiary.class);
    }
}
