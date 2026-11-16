package ru.ranepa.airline.domain;

public class EconomyTicket extends Ticket {
    private final double handLuggageAllowance;

    public EconomyTicket(Flight flight, Passenger passenger, long price, double handLuggageAllowance) {
        super(flight, passenger, price);
        if (!Double.isFinite(handLuggageAllowance) || handLuggageAllowance < 0) {
            throw new IllegalArgumentException("Норма ручной клади должна быть неотрицательной");
        }
        this.handLuggageAllowance = handLuggageAllowance;
    }

    @Override
    public String getDescription() {
        return "Эконом, только ручная кладь: " + handLuggageAllowance + " кг";
    }
}
