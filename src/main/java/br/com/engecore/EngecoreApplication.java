package br.com.engecore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "br.com.engecore")
@EnableScheduling
public class EngecoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(EngecoreApplication.class, args);
    }


}
