package ru.ranepa.airline.domain;

public abstract class Ticket {
    private final Flight flight;
    private final Passenger passenger;
    private final long price;

    public Ticket(Flight flight, Passenger passenger, long price) {
        if (flight == null || passenger == null) {
            throw new IllegalArgumentException("Нужно выбрать рейс и пассажира");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше нуля");
        }
        this.flight = flight;
        this.passenger = passenger;
        this.price = price;
    }

    public Flight getFlight() { return flight; }
    public Passenger getPassenger() { return passenger; }
    // Цена хранится в копейках, чтобы суммы считались точно.
    public long getPrice() { return price; }
    public abstract String getDescription();

    @Override
    public String toString() {
        return flight.getNumber() + " | " + passenger.getName() + " | "
                + String.format("%d.%02d руб.", price / 100, price % 100)
                + " | " + getDescription();
    }
}
