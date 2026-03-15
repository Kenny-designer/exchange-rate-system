package com.exchangerate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeRateSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRateSystemApplication.class, args);
    }
}
