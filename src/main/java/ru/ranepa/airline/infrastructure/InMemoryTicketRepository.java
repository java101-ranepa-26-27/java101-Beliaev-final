package ru.ranepa.airline.infrastructure;

import java.util.ArrayList;
import java.util.List;
import ru.ranepa.airline.application.port.TicketRepository;
import ru.ranepa.airline.domain.Ticket;

public class InMemoryTicketRepository implements TicketRepository {
    private final List<Ticket> items = new ArrayList<>();

    @Override
    public void add(Ticket ticket) { items.add(ticket); }

    @Override
    public void remove(Ticket ticket) { items.remove(ticket); }

    @Override
    public List<Ticket> findAll() { return new ArrayList<>(items); }
}
