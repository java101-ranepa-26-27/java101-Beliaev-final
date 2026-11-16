package ru.ranepa.airline.infrastructure;

import java.util.ArrayList;
import java.util.List;
import ru.ranepa.airline.application.port.FlightRepository;
import ru.ranepa.airline.domain.Flight;

public class InMemoryFlightRepository implements FlightRepository {
    private final List<Flight> items = new ArrayList<>();

    @Override
    public void add(Flight item) { items.add(item); }

    @Override
    public List<Flight> findAll() { return new ArrayList<>(items); }

    @Override
    public Flight findByNumber(String number) {
        for (Flight item : items) {
            if (item.getNumber().equalsIgnoreCase(number)) {
                return item;
            }
        }
        return null;
    }
}
