package ru.ranepa.airline.infrastructure;

import java.util.ArrayList;
import java.util.List;
import ru.ranepa.airline.application.port.PassengerRepository;
import ru.ranepa.airline.domain.Passenger;

public class InMemoryPassengerRepository implements PassengerRepository {
    private final List<Passenger> items = new ArrayList<>();

    @Override
    public void add(Passenger item) { items.add(item); }

    @Override
    public List<Passenger> findAll() { return new ArrayList<>(items); }

    @Override
    public Passenger findByPassport(String passportNumber) {
        for (Passenger item : items) {
            if (item.getPassportNumber().equals(passportNumber)) {
                return item;
            }
        }
        return null;
    }
}
