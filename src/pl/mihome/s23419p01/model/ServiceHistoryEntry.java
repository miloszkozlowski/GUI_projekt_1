package pl.mihome.s23419p01.model;

import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.rent.CarServiceSpot;
import pl.mihome.s23419p01.model.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServiceHistoryEntry {

    private final BigDecimal cost;
    private final CarServiceSpot carServiceSpot;
    private final Vehicle vehicle;
    private final Person person;
    private final LocalDate serviceStartDate;

    public ServiceHistoryEntry(BigDecimal cost, CarServiceSpot carServiceSpot, Vehicle vehicle, LocalDate serviceStartDate) {
        this.cost = cost;
        this.carServiceSpot = carServiceSpot;
        this.vehicle = vehicle;
        this.person = vehicle.getOwner();
        this.serviceStartDate = serviceStartDate;
    }

    public CarServiceSpot getCarServiceSpot() {
        return carServiceSpot;
    }

    public LocalDate getServiceStartDate() {
        return serviceStartDate;
    }

    @Override
    public String toString() {
        return serviceStartDate + ": " + vehicle.toString() + " owned by " + vehicle.getOwner() + " (" + person.getName() + "), PLN " + cost;
    }
}
