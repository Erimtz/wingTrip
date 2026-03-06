package com.wingtrip.payment.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${spring.application.name:api-payment}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Payment Service")
                        .version("1.0.0")
                        .description("API para gestión de pagos")
                        .contact(new Contact()
                                .name("wingtrip")
                                .email("wingtrip@example.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8083").description("Local server"),
                        new Server().url("http://api-payment:8080").description("Docker server")
                ));
    }
}
