package pl.mihome.s23419p01.model.person;

import pl.mihome.s23419p01.exception.NeverRentException;
import pl.mihome.s23419p01.model.BookEntry;
import pl.mihome.s23419p01.model.info.Info;
import pl.mihome.s23419p01.model.info.TenantAlert;
import pl.mihome.s23419p01.model.vehicle.Vehicle;
import pl.mihome.s23419p01.service.DataStock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Person {

    private final List<Info> alerts = new ArrayList<>();
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    private final String firstName;
    private final String lastName;
    private final String pesel;
    private final String residenceAddress;
    private final LocalDate birthDate;
    private LocalDate firstRentalDate;

    public Person(String firstName, String lastName, String pesel, String residenceAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.residenceAddress = residenceAddress;
        int month = Integer.parseInt(pesel.substring(2, 4));
        int year = 1900 + Integer.parseInt(pesel.substring(0, 2));
        if(month>12) {
            year = 2000 + Integer.parseInt(pesel.substring(0, 2));
            month -= 20;
        }
        birthDate =  LocalDate.of(year, (month == 1 ? 12 : month-1), Integer.parseInt(pesel.substring(4, 6)));
    }

    public BigDecimal getUnpaidExpensesTotal() {
        DataStock ds = DataStock.getInstance();
        return ds.getPaymentsBook().stream()
                .filter(entry -> !entry.isPaid() && entry.getPersonId().equals(id))
                .map(BookEntry::getAmountDue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void alert(TenantAlert alert) {
        this.alerts.add(alert);
    }

    public UUID getId() {
        return id;
    }

    public List<Info> getAlerts() {
        return alerts;
    }

    public void removeAlerts(Set<TenantAlert> toRemove) {
        alerts.removeAll(toRemove);
    }

    public LocalDate getFirstRentalDate() {
        if(firstRentalDate==null) {
            throw new NeverRentException();
        }
        return firstRentalDate;
    }

    public void setFirstRentalDate(LocalDate firstRentalDate) {
        if(firstRentalDate==null) {
            this.firstRentalDate = firstRentalDate;
        }
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public Set<BookEntry> getPayments() {
        DataStock dataStock = DataStock.getInstance();
        return dataStock.getPaymentsBook().stream()
                .filter(entry -> entry.getPersonId().equals(id))
                .collect(Collectors.toSet());
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return getName() + ", born " + birthDate.toString() + ", address " + residenceAddress + " (PESEL: " + pesel + "), first rental date: " + (firstRentalDate == null ? "never ever" : firstRentalDate.toString()) + ", assigned unique ID: " + id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId().equals(id);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicle.setOwner(this);
        vehicles.add(vehicle);
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}
