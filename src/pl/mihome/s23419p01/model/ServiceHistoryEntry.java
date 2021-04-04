package pl.mihome.s23419p01.model;

import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.rent.CarServiceSpot;
import pl.mihome.s23419p01.model.vehicle.Vehicle;

import java.math.BigDecimal;

public class ServiceHistoryEntry {

    private final BigDecimal cost;
    private final CarService carService;
    private final CarServiceSpot carServiceSpot;
    private final Vehicle vehicle;
    private final Person person;

    public ServiceHistoryEntry(BigDecimal cost, CarService carService, CarServiceSpot carServiceSpot, Vehicle vehicle, Person person) {
        this.cost = cost;
        this.carService = carService;
        this.carServiceSpot = carServiceSpot;
        this.vehicle = vehicle;
        this.person = person;
    }
}
