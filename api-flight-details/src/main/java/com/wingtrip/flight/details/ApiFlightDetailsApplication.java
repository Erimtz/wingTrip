package com.wingtrip.flight.details;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.wingtrip.flight.details")
public class ApiFlightDetailsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiFlightDetailsApplication.class, args);
    }
}