package ru.ranepa.airline.application;

import java.util.List;
import ru.ranepa.airline.application.port.PassengerRepository;
import ru.ranepa.airline.domain.Passenger;
import ru.ranepa.airline.domain.Validation;

public class PassengerService {
    private final PassengerRepository repository;

    public PassengerService(PassengerRepository repository) {
        this.repository = repository;
    }

    public void addPassenger(String name, String passportNumber, String phone) {
        Passenger passenger = new Passenger(name, passportNumber, phone);
        if (repository.findByPassport(passenger.getPassportNumber()) != null) {
            throw new IllegalArgumentException("Пассажир с таким паспортом уже существует");
        }
        repository.add(passenger);
    }
    public Passenger getPassenger(String passportNumber) {
        String value = Validation.requireText(passportNumber, "Паспорт");
        Passenger passenger = repository.findByPassport(value);
        if (passenger == null) {
            throw new IllegalArgumentException("Пассажир не найден");
        }
        return passenger;
    }

    public List<Passenger> getPassengers() { return repository.findAll(); }
}
