package pl.mihome.s23419p01.model.vehicle;

import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.rent.CarServiceSpot;
import pl.mihome.s23419p01.service.DataStock;

import java.math.BigDecimal;
import java.util.Optional;

public abstract class Vehicle {

    protected final String name;
    protected final BigDecimal sizeCubicMeters;
    protected Person owner;

    public Vehicle(String name, BigDecimal sizeCubicMeters) {
        this.name = name;
        this.sizeCubicMeters = sizeCubicMeters;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Person getOwner() {
        return owner;
    }


    public Optional<CarServiceSpot> getVehicleServiceSpot() {
        DataStock dataStock = DataStock.getInstance();
        return dataStock.getCarServicesWithOwners().keySet().stream()
                .flatMap(carService -> carService.getServiceSpots().stream())
                .filter(spot -> {
                    Vehicle v = spot.getVehicleOn();
                    if(v == null) {
                        return false;
                    }
                    if(v.equals(this)) {
                        return true;
                    }
                    return false;
                })
                .findFirst();
    }

}
