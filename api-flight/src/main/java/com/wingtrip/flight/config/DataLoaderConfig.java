package com.wingtrip.flight.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingtrip.flight.model.FlightEntity;
import com.wingtrip.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataLoaderConfig {

    private final ObjectMapper objectMapper;
    private final FlightRepository flightRepository;

    @Bean
    public CommandLineRunner loadFlightData() {
        return args -> {
            try {
                // Verificar si ya hay datos cargados
                if (flightRepository.count() > 0) {
                    log.info("✅ Flight data already exists in MongoDB. Skipping initialization.");
                    return;
                }

                log.info("📦 Loading flight data from data.json...");

                // Leer el archivo JSON
                ClassPathResource resource = new ClassPathResource("data.json");
                InputStream inputStream = resource.getInputStream();

                // Parsear el JSON
                Map<String, List<FlightEntity>> dataMap = objectMapper.readValue(
                    inputStream,
                    new TypeReference<Map<String, List<FlightEntity>>>() {}
                );

                List<FlightEntity> flightEntities = dataMap.get("flights");

                if (flightEntities != null && !flightEntities.isEmpty()) {
                    flightRepository.saveAll(flightEntities);
                    log.info("✅ {} flights loaded successfully into MongoDB", flightEntities.size());
                } else {
                    log.warn("⚠️ No flights found in data.json");
                }

            } catch (Exception e) {
                log.error("❌ Error loading flight data: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to load flight data", e);
            }
        };
    }
}

