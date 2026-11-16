package ru.ranepa.airline.infrastructure;

import ru.ranepa.airline.application.AirlineOffice;
import ru.ranepa.airline.domain.Flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner;
    private final AirlineOffice office;
    private final DateTimeFormatter dateFormat = DateTimeFormatter
            .ofPattern("dd.MM.uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public ConsoleMenu(Scanner scanner, AirlineOffice office) {
        this.scanner = scanner;
        this.office = office;
    }

    public void run() {
        System.out.println("Добро пожаловать в авиакассу!");
        while (true) {
            System.out.println("\n1. Добавить рейс\n2. Добавить рейс с настройками по умолчанию"
                    + "\n3. Показать рейсы\n4. Добавить пассажира\n5. Показать пассажиров"
                    + "\n6. Продать эконом\n7. Продать бизнес\n8. Вернуть билет"
                    + "\n9. Показать билеты\n10. Найти рейсы по направлению"
                    + "\n11. Рейсы со свободными местами\n12. Статистика рейса\n0. Выход");
            try {
                String command = readText("Выберите пункт: ");
                switch (command) {
                    case "1": addFlight(false); break;
                    case "2": addFlight(true); break;
                    case "3": show(office.getFlights()); break;
                    case "4":
                        office.addPassenger(readText("Имя: "), readText("Паспорт: "), readText("Телефон: "));
                        System.out.println("Пассажир добавлен.");
                        break;
                    case "5": show(office.getPassengers()); break;
                    case "6": sellTicket(false); break;
                    case "7": sellTicket(true); break;
                    case "8":
                        boolean returned = office.returnTicket(readText("Номер рейса: "), readText("Паспорт: "));
                        System.out.println(returned ? "Билет возвращён." : "Билет не найден.");
                        break;
                    case "9": show(office.getTickets()); break;
                    case "10": show(office.findByDirection(readText("Город назначения: "))); break;
                    case "11": show(office.getAvailableFlights()); break;
                    case "12": showStatistics(); break;
                    case "0": System.out.println("Программа завершена."); return;
                    default: System.out.println("Нет такого пункта. Введите номер из меню.");
                }
            } catch (DateTimeParseException exception) {
                System.out.println("Неверная дата. Формат: дд.мм.гггг чч:мм");
            } catch (NumberFormatException exception) {
                System.out.println("Неверное число.");
            } catch (IllegalArgumentException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            } catch (ArithmeticException exception) {
                System.out.println("Слишком большая сумма или больше двух знаков после запятой.");
            } catch (NoSuchElementException exception) {
                System.out.println("Ввод завершён. Программа завершена.");
                return;
            }
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void addFlight(boolean shortForm) {
        String number = readText("Номер рейса: ");
        String origin = readText("Откуда: ");
        String destination = readText("Куда: ");
        if (shortForm) {
            office.addFlight(number, origin, destination);
        } else {
            LocalDateTime departure = LocalDateTime.parse(readText("Вылет (дд.мм.гггг чч:мм): "), dateFormat);
            int capacity = Integer.parseInt(readText("Всего мест: "));
            office.addFlight(number, origin, destination, departure, capacity);
        }
        System.out.println("Рейс добавлен.");
    }

    private void sellTicket(boolean business) {
        String number = readText("Номер рейса: ");
        String passport = readText("Паспорт: ");
        long price = parsePrice(readText("Цена в рублях: "));
        if (business) {
            String meal = readText("Питание: ");
            double baggage = Double.parseDouble(readText("Багаж, кг: ").replace(',', '.'));
            office.sellBusinessTicket(number, passport, price, meal, baggage);
        } else {
            double handLuggage = Double.parseDouble(readText("Ручная кладь, кг: ").replace(',', '.'));
            office.sellEconomyTicket(number, passport, price, handLuggage);
        }
        System.out.println("Билет продан.");
    }

    static long parsePrice(String text) {
        String value = text.trim().replace(',', '.');
        if (!value.matches("[0-9]+([.][0-9]{1,2})?")) {
            throw new IllegalArgumentException("Введите цену в рублях, не более двух знаков после запятой");
        }
        return new BigDecimal(value).movePointRight(2).longValueExact();
    }

    private void show(List<?> items) {
        if (items.isEmpty()) {
            System.out.println("Ничего не найдено.");
            return;
        }
        for (Object item : items) {
            System.out.println(item);
        }
    }

    private void showStatistics() {
        String number = readText("Номер рейса: ");
        Flight flight = office.getFlight(number);
        long revenue = office.getRevenue(number);
        System.out.println(flight);
        System.out.printf("Выручка: %d.%02d руб. Загрузка: %.1f%%. Свободно: %d%n",
                revenue / 100, revenue % 100, office.getLoadFactor(number), flight.getFreeSeats());
        System.out.println("Всего создано рейсов: " + Flight.getCreatedCount());
    }
}
