package pl.mihome.s23419p01.model.vehicle;

import pl.mihome.s23419p01.model.person.Person;

import java.math.BigDecimal;

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
}
