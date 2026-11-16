package ru.ranepa.airline.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Flight {
    private static int createdCount;
    private final String number;
    private final String origin;
    private final String destination;
    private final LocalDateTime departureTime;
    private final int capacity;
    private int freeSeats;

    public Flight(String number, String origin, String destination,
                  LocalDateTime departureTime, int capacity) {
        this.number = Validation.requireText(number, "Номер рейса");
        this.origin = Validation.requireText(origin, "Город отправления");
        this.destination = Validation.requireText(destination, "Город назначения");
        if (departureTime == null || !departureTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Время вылета должно быть в будущем");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Вместимость должна быть больше нуля");
        }
        this.departureTime = departureTime;
        this.capacity = capacity;
        freeSeats = capacity;
        createdCount++;
    }

    public Flight(String number, String origin, String destination) {
        this(number, origin, destination, LocalDateTime.now().plusDays(1).withSecond(0).withNano(0), 100);
    }

    public void takeSeat() {
        if (!departureTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Рейс уже вылетел");
        }
        if (freeSeats == 0) {
            throw new IllegalArgumentException("Свободных мест нет");
        }
        freeSeats--;
    }

    public void releaseSeat() {
        if (freeSeats == capacity) {
            throw new IllegalArgumentException("Все места уже свободны");
        }
        freeSeats++;
    }

    public String getNumber() { return number; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public int getCapacity() { return capacity; }
    public int getFreeSeats() { return freeSeats; }
    public static int getCreatedCount() { return createdCount; }

    @Override
    public String toString() {
        return number + " | " + origin + " — " + destination + " | "
                + departureTime.format(DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm"))
                + " | свободно " + freeSeats + " из " + capacity;
    }
}
