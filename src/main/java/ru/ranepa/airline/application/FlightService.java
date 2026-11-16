package ru.ranepa.airline.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ru.ranepa.airline.application.port.FlightRepository;
import ru.ranepa.airline.domain.Flight;
import ru.ranepa.airline.domain.Validation;

public class FlightService {
    private final FlightRepository repository;

    public FlightService(FlightRepository repository) {
        this.repository = repository;
    }

    public void addFlight(String number, String origin, String destination,
                          LocalDateTime departureTime, int capacity) {
        checkFlightNumber(number);
        repository.add(new Flight(number, origin, destination, departureTime, capacity));
    }

    public void addFlight(String number, String origin, String destination) {
        checkFlightNumber(number);
        repository.add(new Flight(number, origin, destination));
    }

    private void checkFlightNumber(String number) {
        String value = Validation.requireText(number, "Номер рейса");
        if (repository.findByNumber(value) != null) {
            throw new IllegalArgumentException("Рейс с таким номером уже существует");
        }
    }
    public Flight getFlight(String number) {
        String value = Validation.requireText(number, "Номер рейса");
        Flight flight = repository.findByNumber(value);
        if (flight == null) {
            throw new IllegalArgumentException("Рейс не найден");
        }
        return flight;
    }
    public List<Flight> getFlights() { return repository.findAll(); }

    public List<Flight> findByDirection(String destination) {
        String value = Validation.requireText(destination, "Направление");
        List<Flight> result = new ArrayList<>();
        for (Flight flight : repository.findAll()) {
            if (flight.getDestination().equalsIgnoreCase(value)) {
                result.add(flight);
            }
        }
        return result;
    }

    public List<Flight> getAvailableFlights() {
        List<Flight> result = new ArrayList<>();
        for (Flight flight : repository.findAll()) {
            if (flight.getFreeSeats() > 0 && flight.getDepartureTime().isAfter(LocalDateTime.now())) {
                result.add(flight);
            }
        }
        return result;
    }
    public double getLoadFactor(String number) {
        Flight flight = getFlight(number);
        return (flight.getCapacity() - flight.getFreeSeats()) * 100.0 / flight.getCapacity();
    }
}
