package com.wingtrip.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.wingtrip.booking")
public class ApiBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiBookingApplication.class, args);
    }
}