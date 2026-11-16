package ru.ranepa.airline.application;

import java.util.List;
import ru.ranepa.airline.application.port.TicketRepository;
import ru.ranepa.airline.domain.Flight;
import ru.ranepa.airline.domain.Passenger;
import ru.ranepa.airline.domain.Ticket;
import ru.ranepa.airline.domain.EconomyTicket;
import ru.ranepa.airline.domain.BusinessTicket;

public class TicketService {
    private final TicketRepository repository;
    private final FlightService flights;
    private final PassengerService passengers;

    public TicketService(TicketRepository repository, FlightService flights, PassengerService passengers) {
        this.repository = repository;
        this.flights = flights;
        this.passengers = passengers;
    }

    public void sellEconomyTicket(String number, String passport, long price, double handLuggage) {
        sell(new EconomyTicket(flights.getFlight(number), passengers.getPassenger(passport), price, handLuggage));
    }

    public void sellBusinessTicket(String number, String passport, long price, String meal, double baggage) {
        sell(new BusinessTicket(flights.getFlight(number), passengers.getPassenger(passport), price, meal, baggage));
    }

    private void sell(Ticket ticket) {
        for (Ticket existing : repository.findAll()) {
            if (existing.getFlight() == ticket.getFlight()
                    && existing.getPassenger() == ticket.getPassenger()) {
                throw new IllegalArgumentException("У пассажира уже есть билет на этот рейс");
            }
        }
        // Проверяем переполнение суммы до изменения числа мест.
        Math.addExact(getRevenue(ticket.getFlight().getNumber()), ticket.getPrice());
        ticket.getFlight().takeSeat();
        repository.add(ticket);
    }

    public boolean returnTicket(String number, String passport) {
        Flight flight = flights.getFlight(number);
        Passenger passenger = passengers.getPassenger(passport);
        for (Ticket ticket : repository.findAll()) {
            if (ticket.getFlight() == flight && ticket.getPassenger() == passenger) {
                flight.releaseSeat();
                repository.remove(ticket);
                return true;
            }
        }
        return false;
    }

    public long getRevenue(String number) {
        Flight flight = flights.getFlight(number);
        long revenue = 0;
        for (Ticket ticket : repository.findAll()) {
            if (ticket.getFlight() == flight) {
                revenue = Math.addExact(revenue, ticket.getPrice());
            }
        }
        return revenue;
    }

    public List<Ticket> getTickets() { return repository.findAll(); }
}
