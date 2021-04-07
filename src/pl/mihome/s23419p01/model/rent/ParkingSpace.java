package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ParkingSpace extends RentableArea {

    public static final Byte MAX_RENTAL_LENGTH_DAYS = 14;

    private Vehicle vehicleParked;

    public ParkingSpace(BigDecimal areaCubicMeters, BigDecimal dailyPrice) {
        super(areaCubicMeters, dailyPrice);
    }

    public ParkingSpace(BigDecimal lengthMeters, BigDecimal widthMeters, BigDecimal heightMeters, BigDecimal dailyPrice) {
        super(lengthMeters, widthMeters, heightMeters, dailyPrice);
    }

    @Override
    public ParkingSpace rent(Person person, LocalDate from, LocalDate until) throws IllegalAccessException {
        if(until.minusDays(MAX_RENTAL_LENGTH_DAYS).isAfter(from)) {
            throw new IllegalArgumentException("Yo! This is much too long for renting a parking space here!");
        }
        return (ParkingSpace)super.rent(person, from, until);
    }

    @Override
    public boolean refreshCurrentState() {
        boolean isRentalEnded = super.refreshCurrentState();
        if(isCarParked() && isRentalEnded) {
            moveTheCarOut();
        }
        return isRentalEnded;
    }

    public void moveTheCarIn(Vehicle vehicle) {
        if(isCarParked()) {
            throw new IllegalStateException("Hey yo! We have other car here: " + vehicleParked.toString());
        }
        vehicleParked = vehicle;
    }

    public void moveTheCarOut() {
        if(!isCarParked()) {
            throw new IllegalStateException("There is no car parked here baby!");
        }
        vehicleParked = null;
    }

    public boolean isCarParked() {
        return vehicleParked != null;
    }

    public Vehicle getVehicleParked() {
        return vehicleParked;
    }

    @Override
    public String toString() {
        return "Parking Space, space: " + areaCubicMeters + "m3, price: PLN " + dailyPrice + " per day";
    }
}
