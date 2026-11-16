package ru.ranepa.airline.application;

import ru.ranepa.airline.TestOfficeFactory;
import ru.ranepa.airline.application.AirlineOffice;
import ru.ranepa.airline.domain.Flight;
import ru.ranepa.airline.domain.Passenger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AirlineOfficeTest {
    private AirlineOffice office;

    @BeforeEach
    void setUp() {
        office = TestOfficeFactory.create();
        office.addFlight("SU1", "Москва", "Казань", LocalDateTime.now().plusDays(10), 2);
        office.addPassenger("Иван", "123", "89000000000");
        office.addPassenger("Анна", "456", "89111111111");
    }

    @Test
    void saleAndRefundChangeSeatsRevenueAndLoad() {
        office.sellEconomyTicket("SU1", "123", 125050, 10);
        assertEquals(1, office.getFlight("SU1").getFreeSeats());
        assertEquals(125050, office.getRevenue("SU1"));
        assertEquals(50.0, office.getLoadFactor("SU1"));
        assertTrue(office.returnTicket("SU1", "123"));
        assertEquals(2, office.getFlight("SU1").getFreeSeats());
        assertEquals(0, office.getRevenue("SU1"));
        assertEquals(0.0, office.getLoadFactor("SU1"));
        assertTrue(office.getTickets().isEmpty());
        assertFalse(office.returnTicket("SU1", "123"));
        assertEquals(2, office.getFlight("SU1").getFreeSeats());
    }

    @Test
    void fullFlightCannotSellAnotherTicket() {
        office.sellEconomyTicket("SU1", "123", 10000, 5);
        office.sellBusinessTicket("SU1", "456", 20000, "Обед", 20);
        office.addPassenger("Пётр", "789", "89222222222");
        assertThrows(IllegalArgumentException.class,
                () -> office.sellEconomyTicket("SU1", "789", 30000, 5));
        assertEquals(0, office.getFlight("SU1").getFreeSeats());
        assertEquals(2, office.getTickets().size());
        assertEquals(30000, office.getRevenue("SU1"));
        assertEquals(100.0, office.getLoadFactor("SU1"));
        assertTrue(office.getAvailableFlights().isEmpty());
    }

    @Test
    void invalidPricesDoNotReserveSeat() {
        assertThrows(IllegalArgumentException.class,
                () -> office.sellEconomyTicket("SU1", "123", 0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> office.sellEconomyTicket("SU1", "123", -100, 5));
        assertEquals(2, office.getFlight("SU1").getFreeSeats());
        assertTrue(office.getTickets().isEmpty());
    }

    @Test
    void invalidBusinessDataDoesNotReserveSeat() {
        assertThrows(IllegalArgumentException.class,
                () -> office.sellBusinessTicket("SU1", "123", 10000, " ", 20));
        assertThrows(IllegalArgumentException.class,
                () -> office.sellBusinessTicket("SU1", "123", 10000, "Обед", -1));
        assertThrows(IllegalArgumentException.class,
                () -> office.sellEconomyTicket("SU1", "123", 10000, Double.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> office.sellBusinessTicket("SU1", "123", 10000, "Обед", Double.POSITIVE_INFINITY));
        assertEquals(2, office.getFlight("SU1").getFreeSeats());
        assertTrue(office.getTickets().isEmpty());
    }

    @Test
    void pastFlightAndInvalidCapacityAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> office.addFlight("SU2", "Москва", "Омск", LocalDateTime.now().minusDays(1), 10));
        assertThrows(IllegalArgumentException.class,
                () -> office.addFlight("SU2", "Москва", "Омск", LocalDateTime.now().plusDays(1), 0));
        assertEquals(1, office.getFlights().size());
    }

    @Test
    void duplicatePassportAndFlightAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> office.addPassenger("Другое имя", " 123 ", "89999999999"));
        assertThrows(IllegalArgumentException.class,
                () -> office.addFlight(" su1 ", "Москва", "Омск"));
        assertEquals(2, office.getPassengers().size());
        assertEquals(1, office.getFlights().size());
    }

    @Test
    void duplicateSaleIsRejected() {
        office.sellEconomyTicket("SU1", "123", 10000, 5);
        assertThrows(IllegalArgumentException.class,
                () -> office.sellBusinessTicket("SU1", "123", 20000, "Обед", 20));
        assertEquals(1, office.getTickets().size());
        assertEquals(1, office.getFlight("SU1").getFreeSeats());
    }

    @Test
    void missingFlightOrPassengerDoesNotChangeData() {
        assertThrows(IllegalArgumentException.class,
                () -> office.sellEconomyTicket("XXX", "123", 10000, 5));
        assertThrows(IllegalArgumentException.class,
                () -> office.sellEconomyTicket("SU1", "XXX", 10000, 5));
        assertThrows(IllegalArgumentException.class, () -> office.getRevenue("XXX"));
        assertEquals(2, office.getFlight("SU1").getFreeSeats());
        assertTrue(office.getTickets().isEmpty());
    }

    @Test
    void searchIgnoresCaseAndHandlesNoMatches() {
        assertEquals(1, office.findByDirection(" казань ").size());
        assertTrue(office.findByDirection("Омск").isEmpty());
        assertEquals(1, office.getAvailableFlights().size());
        AirlineOffice empty = TestOfficeFactory.create();
        assertTrue(empty.findByDirection("Казань").isEmpty());
        assertTrue(empty.getAvailableFlights().isEmpty());
    }

    @Test
    void returnedListsCannotRemoveStoredData() {
        office.getFlights().clear();
        office.getPassengers().clear();
        office.sellEconomyTicket("SU1", "123", 10000, 5);
        office.getTickets().clear();
        assertEquals(1, office.getFlights().size());
        assertEquals(2, office.getPassengers().size());
        assertEquals(1, office.getTickets().size());
    }

    @Test
    void bothTicketTypesHaveDifferentDescriptions() {
        office.sellEconomyTicket("SU1", "123", 10000, 5);
        office.sellBusinessTicket("SU1", "456", 20000, "Обед", 20);
        assertTrue(office.getTickets().get(0).getDescription().contains("ручная кладь"));
        assertTrue(office.getTickets().get(1).getDescription().contains("Обед"));
        assertTrue(office.getTickets().get(1).getDescription().contains("20.0"));
    }

    @Test
    void defaultConstructorAndCounterWork() {
        int before = Flight.getCreatedCount();
        office.addFlight("SU2", "Москва", "Омск");
        Flight flight = office.getFlight("SU2");
        assertEquals(100, flight.getCapacity());
        assertEquals(100, flight.getFreeSeats());
        assertTrue(flight.getDepartureTime().isAfter(LocalDateTime.now()));
        assertEquals(before + 1, Flight.getCreatedCount());
        assertThrows(IllegalArgumentException.class, () -> office.addFlight("SU2", "Москва", "Омск"));
        assertEquals(before + 1, Flight.getCreatedCount());
    }

    @Test
    void seatCountCannotExceedCapacity() {
        Flight flight = office.getFlight("SU1");
        assertThrows(IllegalArgumentException.class, flight::releaseSeat);
        assertEquals(2, flight.getFreeSeats());
    }

    @Test
    void revenueOverflowDoesNotReserveSeat() {
        office.sellEconomyTicket("SU1", "123", Long.MAX_VALUE, 5);
        assertThrows(ArithmeticException.class,
                () -> office.sellEconomyTicket("SU1", "456", 1, 5));
        assertEquals(1, office.getFlight("SU1").getFreeSeats());
        assertEquals(1, office.getTickets().size());
        assertEquals(Long.MAX_VALUE, office.getRevenue("SU1"));
    }

    @Test
    void emptyFieldsAndInvalidPassengerEditsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> office.addPassenger(" ", "789", "123"));
        assertThrows(IllegalArgumentException.class, () -> office.addPassenger("Имя", "789", null));
        assertThrows(IllegalArgumentException.class, () -> office.addFlight("SU2", " ", "Омск"));
        Passenger passenger = office.getPassenger("123");
        assertThrows(IllegalArgumentException.class, () -> passenger.setName(" "));
        assertEquals("Иван", passenger.getName());
    }
}
