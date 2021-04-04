package pl.mihome.s23419p01.model.vehicle;

import java.math.BigDecimal;

public class OffRoadCar extends Car {

    private final int amountOfAdditionalLights;


    public OffRoadCar(String name, BigDecimal sizeCubicMeters, boolean doesHaveTowbar, int amountOfAdditionalLights) {
        super(name, sizeCubicMeters, doesHaveTowbar);
        this.amountOfAdditionalLights = amountOfAdditionalLights;
    }

    @Override
    public String toString() {
        return "OFF ROAD CAR: Name: " + name + ", size: " + sizeCubicMeters + "m2, has towbar: " + doesHaveTowbar + ", amount of additional lights: " + amountOfAdditionalLights;
    }

}
