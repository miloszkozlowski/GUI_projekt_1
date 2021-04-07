package pl.mihome.s23419p01.model.vehicle;

import java.math.BigDecimal;

public class CityCar extends Car {

    private final int amountOfIsoFixSeats;

    public CityCar(String name, BigDecimal sizeCubicMeters, boolean doesHaveTowbar, int amountOfIsoFixSeats) {
        super(name, sizeCubicMeters, doesHaveTowbar);
        this.amountOfIsoFixSeats = amountOfIsoFixSeats;
    }

    @Override
    public String toString() {
        return "CITY CAR: Name: " + name + ", size: " + sizeCubicMeters + "m3, has towbar: " + doesHaveTowbar + ", amount of seats equipped with ISO-FIX: " + amountOfIsoFixSeats;
    }
}
