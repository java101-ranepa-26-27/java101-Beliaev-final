package ru.ranepa.airline.infrastructure;

import ru.ranepa.airline.TestOfficeFactory;
import ru.ranepa.airline.application.AirlineOffice;
import ru.ranepa.airline.domain.Flight;
import ru.ranepa.airline.domain.Passenger;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class ConsoleMenuTest {
    private String runMenu(String input, AirlineOffice office) {
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(output);
            new ConsoleMenu(new Scanner(input), office).run();
        } finally {
            System.setOut(original);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }

    @Test
    void completeSaleAndRefundThroughMenu() {
        AirlineOffice office = TestOfficeFactory.create();
        String result = runMenu(String.join("\n", "2", "SU1", "Москва", "Казань",
                "4", "Иван", "123", "89000000000", "6", "SU1", "123", "1250,50", "10",
                "9", "12", "SU1", "8", "SU1", "123", "0") + "\n", office);
        assertTrue(result.contains("Билет продан."));
        assertTrue(result.contains("1250.50"));
        assertTrue(result.contains("Билет возвращён."));
        assertEquals(100, office.getFlight("SU1").getFreeSeats());
        assertEquals(0, office.getRevenue("SU1"));
    }

    @Test
    void invalidInputReturnsToMenu() {
        AirlineOffice office = TestOfficeFactory.create();
        String result = runMenu(String.join("\n", "abc", "1", "SU1", "Москва", "Казань",
                "31.02.2099 12:00", "1", "SU2", "Москва", "Омск", "20.12.2099 12:00",
                "abc", "0") + "\n", office);
        assertTrue(result.contains("Нет такого пункта"));
        assertTrue(result.contains("Неверная дата"));
        assertTrue(result.contains("Неверное число"));
        assertTrue(result.contains("Программа завершена"));
        assertTrue(office.getFlights().isEmpty());
    }

    @Test
    void endOfInputInTheMiddleOfCommandExitsWithoutAddingData() {
        AirlineOffice office = TestOfficeFactory.create();
        String result = runMenu("4\nИван\n", office);
        assertTrue(result.contains("Ввод завершён"));
        assertTrue(office.getPassengers().isEmpty());
    }

    @Test
    void priceParsingIsExactAndRejectsInvalidFormats() {
        assertEquals(125050, ConsoleMenu.parsePrice("1250,50"));
        assertEquals(10, ConsoleMenu.parsePrice("0.10"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleMenu.parsePrice("NaN"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleMenu.parsePrice("1.001"));
        assertThrows(ArithmeticException.class, () -> ConsoleMenu.parsePrice("9999999999999999999999"));
    }
}
