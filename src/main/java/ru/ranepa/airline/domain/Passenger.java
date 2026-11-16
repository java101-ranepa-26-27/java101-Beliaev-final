package ru.ranepa.airline.domain;

public class Passenger {
    private String name;
    private final String passportNumber;
    private String phone;

    public Passenger(String name, String passportNumber, String phone) {
        this.name = Validation.requireText(name, "Имя");
        this.passportNumber = Validation.requireText(passportNumber, "Паспорт");
        this.phone = Validation.requireText(phone, "Телефон");
    }

    public String getName() { return name; }
    public String getPassportNumber() { return passportNumber; }
    public String getPhone() { return phone; }
    public void setName(String name) { this.name = Validation.requireText(name, "Имя"); }
    public void setPhone(String phone) { this.phone = Validation.requireText(phone, "Телефон"); }

    @Override
    public String toString() {
        return name + " | паспорт: " + passportNumber + " | телефон: " + phone;
    }
}
