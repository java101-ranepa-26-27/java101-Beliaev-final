package ru.ranepa.airline.application.port;

import java.util.List;
import ru.ranepa.airline.domain.Flight;

public interface FlightRepository {
    void add(Flight item);
    List<Flight> findAll();
    Flight findByNumber(String number);
}
