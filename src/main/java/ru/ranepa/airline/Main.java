package ru.ranepa.airline;

import java.util.Scanner;
import ru.ranepa.airline.application.AirlineOffice;
import ru.ranepa.airline.application.FlightService;
import ru.ranepa.airline.application.PassengerService;
import ru.ranepa.airline.application.TicketService;
import ru.ranepa.airline.infrastructure.ConsoleMenu;
import ru.ranepa.airline.infrastructure.InMemoryFlightRepository;
import ru.ranepa.airline.infrastructure.InMemoryPassengerRepository;
import ru.ranepa.airline.infrastructure.InMemoryTicketRepository;

public class Main {
    public static void main(String[] args) {
        FlightService flights = new FlightService(new InMemoryFlightRepository());
        PassengerService passengers = new PassengerService(new InMemoryPassengerRepository());
        TicketService tickets = new TicketService(new InMemoryTicketRepository(), flights, passengers);
        AirlineOffice office = new AirlineOffice(flights, passengers, tickets);
        new ConsoleMenu(new Scanner(System.in), office).run();
    }
}
1