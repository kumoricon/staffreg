package org.kumoricon.staffserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StaffserverApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaffserverApplication.class, args);
    }
}
