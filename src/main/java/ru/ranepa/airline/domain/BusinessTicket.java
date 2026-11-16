package ru.ranepa.airline.domain;

public class BusinessTicket extends Ticket {
    private final String meal;
    private final double baggageAllowance;

    public BusinessTicket(Flight flight, Passenger passenger, long price,
                          String meal, double baggageAllowance) {
        super(flight, passenger, price);
        this.meal = Validation.requireText(meal, "Питание");
        if (!Double.isFinite(baggageAllowance) || baggageAllowance < 0) {
            throw new IllegalArgumentException("Норма багажа должна быть неотрицательной");
        }
        this.baggageAllowance = baggageAllowance;
    }

    @Override
    public String getDescription() {
        return "Бизнес, питание: " + meal + ", багаж: " + baggageAllowance + " кг";
    }
}
