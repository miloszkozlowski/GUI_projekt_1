package pl.mihome.s23419p01.model.vehicle;

import java.math.BigDecimal;

public abstract class Car extends Vehicle {
    protected final boolean doesHaveTowbar;

    public Car(String name, BigDecimal sizeCubicMeters, boolean doesHaveTowbar) {
        super(name, sizeCubicMeters);
        this.doesHaveTowbar = doesHaveTowbar;
    }
}
