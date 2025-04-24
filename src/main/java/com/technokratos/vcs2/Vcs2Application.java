package com.technokratos.vcs2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class Vcs2Application {

    public static void main(String[] args) {
        SpringApplication.run(Vcs2Application.class, args);
    }

}
