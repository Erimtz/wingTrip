package com.wingtrip.flight.details.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingtrip.flight.details.model.FlightDetailsEntity;
import com.wingtrip.flight.details.repository.FlightDetailsRepository;
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
    private final FlightDetailsRepository flightDetailsRepository;

    @Bean
    public CommandLineRunner loadFlightDetailsData() {
        return args -> {
            try {
                if (flightDetailsRepository.count() > 0) {
                    log.info("✅ Flight details data already exists in MongoDB. Skipping initialization.");
                    return;
                }

                log.info("📦 Loading flight details data from data.json...");

                ClassPathResource resource = new ClassPathResource("data.json");
                InputStream inputStream = resource.getInputStream();

                Map<String, List<FlightDetailsEntity>> dataMap = objectMapper.readValue(
                        inputStream,
                        new TypeReference<Map<String, List<FlightDetailsEntity>>>() {}
                );

                List<FlightDetailsEntity> entities = dataMap.get("flight_details");

                if (entities != null && !entities.isEmpty()) {
                    flightDetailsRepository.saveAll(entities);
                    log.info("✅ {} flight details loaded successfully into MongoDB", entities.size());
                } else {
                    log.warn("⚠️ No flight details found in data.json");
                }

            } catch (Exception e) {
                log.error("❌ Error loading flight details data: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to load flight details data", e);
            }
        };
    }
}
