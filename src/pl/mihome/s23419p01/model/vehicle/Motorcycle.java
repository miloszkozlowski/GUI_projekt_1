package pl.mihome.s23419p01.model.vehicle;

import java.math.BigDecimal;

public class Motorcycle extends Vehicle {

    private final boolean doesHaveSideTrunks;

    public Motorcycle(String name, BigDecimal sizeCubicMeters, boolean doesHaveSideTrunks) {
        super(name, sizeCubicMeters);
        this.doesHaveSideTrunks = doesHaveSideTrunks;
    }

    @Override
    public String toString() {
        return "MOTORCYCLE: Name: " + name + ", size: " + sizeCubicMeters + "m3, has side trunks: " + doesHaveSideTrunks;
    }
}
