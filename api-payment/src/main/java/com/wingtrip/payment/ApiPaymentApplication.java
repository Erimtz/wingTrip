package com.wingtrip.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.wingtrip.payment")
public class ApiPaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiPaymentApplication.class, args);
    }
}