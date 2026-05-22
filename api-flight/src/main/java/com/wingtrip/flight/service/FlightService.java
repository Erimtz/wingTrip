package com.wingtrip.flight.service;

import com.wingtrip.flight.dto.FlightDTO;
import com.wingtrip.flight.exception.FlightNotCreatedException;
import com.wingtrip.flight.exception.FlightNotFoundException;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    /**
     * Obtiene todos los vuelos disponibles
     * @return Lista de todos los vuelos
     */
    List<FlightDTO> getAllFlights() throws FlightNotFoundException;

    /**
     * Obtiene un vuelo específico por su número de vuelo
     * @param flightNumber Número único del vuelo
     * @return Optional conteniendo el vuelo si existe
     */
    Optional<FlightDTO> getFlightById(String flightNumber);

    /**
     * Busca vuelos según criterios de origen, destino y fecha de salida
     * @param origin Ciudad de origen
     * @param destination Ciudad de destino
     * @param departureDate Fecha de salida
     * @return Lista de vuelos que coinciden con los criterios
     */
    List<FlightDTO> searchFlights(String origin, String destination, String departureDate) throws FlightNotFoundException;

    /**
     * Crea un nuevo vuelo
     * @param flightDTO Datos del vuelo a crear
     * @return El vuelo creado con su identificador
     */
    FlightDTO createFlight(FlightDTO flightDTO) throws FlightNotCreatedException;

    /**
     * Actualiza un vuelo existente
     * @param flightNumber Número del vuelo a actualizar
     * @param flightDTO Datos actualizados del vuelo
     * @return El vuelo actualizado
     */
    FlightDTO updateFlight(String flightNumber, FlightDTO flightDTO) throws FlightNotFoundException;

    /**
     * Elimina un vuelo
     * @param flightNumber Número del vuelo a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    boolean deleteFlight(String flightNumber);
}
