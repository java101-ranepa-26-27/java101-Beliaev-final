package ru.ranepa.airline.application;

import java.time.LocalDateTime;
import java.util.List;
import ru.ranepa.airline.domain.Flight;
import ru.ranepa.airline.domain.Passenger;
import ru.ranepa.airline.domain.Ticket;

// Общая точка доступа к кассе для меню.
public class AirlineOffice {
    private final FlightService flights;
    private final PassengerService passengers;
    private final TicketService tickets;

    public AirlineOffice(FlightService flights, PassengerService passengers, TicketService tickets) {
        this.flights = flights;
        this.passengers = passengers;
        this.tickets = tickets;
    }

    public void addFlight(String number, String origin, String destination, LocalDateTime departure, int capacity) {
        flights.addFlight(number, origin, destination, departure, capacity);
    }
    public void addFlight(String number, String origin, String destination) {
        flights.addFlight(number, origin, destination);
    }
    public void addPassenger(String name, String passport, String phone) {
        passengers.addPassenger(name, passport, phone);
    }
    public Flight getFlight(String number) { return flights.getFlight(number); }
    public Passenger getPassenger(String passport) { return passengers.getPassenger(passport); }
    public List<Flight> getFlights() { return flights.getFlights(); }
    public List<Passenger> getPassengers() { return passengers.getPassengers(); }
    public List<Ticket> getTickets() { return tickets.getTickets(); }
    public List<Flight> findByDirection(String destination) { return flights.findByDirection(destination); }
    public List<Flight> getAvailableFlights() { return flights.getAvailableFlights(); }
    public void sellEconomyTicket(String number, String passport, long price, double handLuggage) {
        tickets.sellEconomyTicket(number, passport, price, handLuggage);
    }
    public void sellBusinessTicket(String number, String passport, long price, String meal, double baggage) {
        tickets.sellBusinessTicket(number, passport, price, meal, baggage);
    }
    public boolean returnTicket(String number, String passport) { return tickets.returnTicket(number, passport); }
    public long getRevenue(String number) { return tickets.getRevenue(number); }
    public double getLoadFactor(String number) { return flights.getLoadFactor(number); }
}
