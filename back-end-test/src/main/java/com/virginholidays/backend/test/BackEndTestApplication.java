package com.virginholidays.backend.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * The back end test application
 *
 * @author Geoff Perks
 */
@EnableWebMvc
@SpringBootApplication
public class BackEndTestApplication {

    /**
     * Entry point
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BackEndTestApplication.class, args);
    }
}
