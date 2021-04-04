package pl.mihome.s23419p01.model.vehicle;

import java.math.BigDecimal;

public class Amphibian extends Vehicle {

    private final String sideColor;


    public Amphibian(String name, BigDecimal sizeCubicMeters, String sideColor) {
        super(name, sizeCubicMeters);
        this.sideColor = sideColor;
    }

    @Override
    public String toString() {
        return "AMPHIBIAN: Name: " + name + ", size: " + sizeCubicMeters + "m2, side color: " + sideColor;
    }
}
