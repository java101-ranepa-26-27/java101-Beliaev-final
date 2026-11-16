package ru.ranepa.airline;

import ru.ranepa.airline.application.AirlineOffice;
import ru.ranepa.airline.application.FlightService;
import ru.ranepa.airline.application.PassengerService;
import ru.ranepa.airline.application.TicketService;
import ru.ranepa.airline.infrastructure.InMemoryFlightRepository;
import ru.ranepa.airline.infrastructure.InMemoryPassengerRepository;
import ru.ranepa.airline.infrastructure.InMemoryTicketRepository;

public class TestOfficeFactory {
    public static AirlineOffice create() {
        FlightService flights = new FlightService(new InMemoryFlightRepository());
        PassengerService passengers = new PassengerService(new InMemoryPassengerRepository());
        TicketService tickets = new TicketService(new InMemoryTicketRepository(), flights, passengers);
        return new AirlineOffice(flights, passengers, tickets);
    }
}
