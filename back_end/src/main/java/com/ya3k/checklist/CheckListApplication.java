package com.ya3k.checklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Configuration
@PropertySource("classpath:backend.env")
public class CheckListApplication {

    public static void main(String[] args) {

        SpringApplication.run(CheckListApplication.class, args);

    }

}
