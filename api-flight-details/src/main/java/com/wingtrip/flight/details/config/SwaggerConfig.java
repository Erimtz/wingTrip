package com.wingtrip.flight.details.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
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

    @Value("${spring.application.name:api-flight-details}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Flight Details Service")
                        .version("1.0.0")
                        .description("API para gestión la de detalles de vuelos")
                        .contact(new Contact()
                                .name("wingtrip")
                                .email("wingtrip@example.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local server"),
                        new Server().url("http://api-flight-details:8080").description("Docker server")
                ));
    }
}
