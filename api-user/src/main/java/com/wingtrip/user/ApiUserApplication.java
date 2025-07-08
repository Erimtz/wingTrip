package com.wingtrip.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.wingtrip.user")
public class ApiUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiUserApplication.class, args);
    }
}
