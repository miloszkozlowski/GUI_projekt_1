package pl.mihome.s23419p01.model;

import java.math.BigDecimal;

public class CustomerProperty {

    private final BigDecimal spaceCubicMeters;
    private final java.util.UUID id = java.util.UUID.randomUUID();
    private final String name;

    public CustomerProperty(BigDecimal spaceCubicMeters, String name) {
        this.spaceCubicMeters = spaceCubicMeters;
        this.name = name;
    }

    public CustomerProperty(BigDecimal length, BigDecimal width, BigDecimal height, String name) {
        this(length.multiply(width).multiply(height), name);
    }

    public BigDecimal getSpaceCubicMeters() {
        return spaceCubicMeters;
    }

    public java.util.UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ", " + spaceCubicMeters + "m3 (" + id + ")";
    }
}
