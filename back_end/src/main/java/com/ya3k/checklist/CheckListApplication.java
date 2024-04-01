package com.ya3k.checklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheckListApplication {

    public static void main(String[] args) {

        SpringApplication.run(CheckListApplication.class, args);

    }

}
