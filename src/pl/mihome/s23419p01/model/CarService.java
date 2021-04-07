package pl.mihome.s23419p01.model;

import pl.mihome.s23419p01.model.person.CarServiceOwner;
import pl.mihome.s23419p01.model.rent.*;
import pl.mihome.s23419p01.model.vehicle.Vehicle;
import pl.mihome.s23419p01.service.DataStock;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CarService {

    private final Set<RentableArea> warehousesSet = new HashSet<>();
    private final Set<CarServiceSpot> serviceSpots = new HashSet<>();
    private final Queue<Vehicle> carsWaitingForService = new LinkedList<>();
    private final Queue<Vehicle> carsWaitingForIndependentSpot = new LinkedList<>();
    private final BigDecimal areaSquareMeters;
    private final BigDecimal serviceHeight;
    private final String name;

    public CarService(String name, BigDecimal percentForOwnStock, BigDecimal areaSquareMeters, BigDecimal serviceHeight) {
        this.name = name;
        this.areaSquareMeters = areaSquareMeters;
        this.serviceHeight = serviceHeight;
        ServiceWarehouse serviceWarehouse = new ServiceWarehouse(percentForOwnStock.multiply(areaSquareMeters).movePointLeft(2), serviceHeight, null);
        warehousesSet.add(serviceWarehouse);
    }

    public Set<RentableArea> getWarehousesSet() {
        return this.warehousesSet;
    }

    public CarServiceSpot serviceCar(Vehicle vehicle, boolean independent) {
        Optional<CarServiceSpot> availableCarServiceSpot = getAvailableServiceSpot(independent);
        if (availableCarServiceSpot.isPresent()) {
            availableCarServiceSpot.get().putVehicle(vehicle);
            return availableCarServiceSpot.get();
        } else if (independent) {
            carsWaitingForIndependentSpot.add(vehicle);

        } else {
            carsWaitingForService.add(vehicle);
        }
        return null;
    }

    public Optional<CarServiceSpot> getAvailableServiceSpot(boolean independent) {
        if (independent) {
            return serviceSpots.stream()
                    .filter(spot -> spot instanceof IndependentCarServiceSpot)
                    .filter(CarServiceSpot::isAvailable)
                    .findFirst();
        } else {
            return serviceSpots.stream()
                    .filter(spot -> !(spot instanceof IndependentCarServiceSpot))
                    .filter(CarServiceSpot::isAvailable)
                    .findFirst();
        }
    }

    public Queue<Vehicle> getCarsWaitingForService() {
        return carsWaitingForService;
    }

    public Set<CarServiceSpot> getServiceSpots() {
        return serviceSpots;
    }

    public Queue<Vehicle> getCarsWaitingForIndependentSpot() {
        return carsWaitingForIndependentSpot;
    }

    public void addWarehouse(RentableArea rentableArea) {
        if (getCurrentlyOccupiedSpace().add(rentableArea.getAreaCubicMeters()).compareTo(areaSquareMeters.multiply(serviceHeight)) > 0) {
            throw new IllegalStateException("No space for this warehouse in this service");
        }
        warehousesSet.add(rentableArea);
    }

    public void addServiceSpot(CarServiceSpot carServiceSpot) {
        if (getCurrentlyOccupiedSpace().add(CarServiceSpot.SPACE_CUBIC_METERS).compareTo(areaSquareMeters.multiply(serviceHeight)) > 0) {
            throw new IllegalStateException("No space for this spot in this service");
        }
        serviceSpots.add(carServiceSpot);
    }

    public BigDecimal getCurrentlyOccupiedSpace() {
        return warehousesSet.stream()
                .map(RentableArea::getAreaCubicMeters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ConsumerWarehouse> getListOfAvailableConsumerWarehouses() {
        return warehousesSet.stream()
                .filter(warehouse -> warehouse instanceof ConsumerWarehouse)
                .filter(RentableArea::isAvailableForRent)
                .map(warehouse -> (ConsumerWarehouse) warehouse)
                .collect(Collectors.toList());
    }

    public List<ParkingSpace> getListOfAvailableParkingSpaces() {
        return warehousesSet.stream()
                .filter(warehouse -> warehouse instanceof ParkingSpace)
                .filter(RentableArea::isAvailableForRent)
                .map(warehouse -> (ParkingSpace) warehouse)
                .collect(Collectors.toList());
    }

    public ParkingSpace getAvailableParkingSpace() {
        List<ParkingSpace> available = getListOfAvailableParkingSpaces();
        if (available.size() > 0) {
            return available.get(0);
        }
        return null;
    }

    public ConsumerWarehouse getAvailableConsumerWarehouse() {
        List<ConsumerWarehouse> available = getListOfAvailableConsumerWarehouses();
        if (available.size() > 0) {
            return available.get(0);
        }
        return null;
    }

    private CarServiceOwner getOwner() {
        return DataStock.getInstance().getCarServicesWithOwners().get(this);
    }


    @Override
    public String toString() {
        return name + " - " + getOwner().getName();
    }
}
