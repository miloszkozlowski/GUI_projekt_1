package pl.mihome.s23419p01.service;

import pl.mihome.s23419p01.model.ServiceHistoryEntry;
import pl.mihome.s23419p01.model.rent.CarServiceSpot;
import pl.mihome.s23419p01.model.vehicle.Vehicle;

import java.util.Optional;

public class CarServiceSpotsManager {

    private final DataStock dataStock;

    public CarServiceSpotsManager() {
        dataStock = DataStock.getInstance();
    }

    public void refresh() {
        dataStock.getCarServicesWithOwners().keySet()
                .stream()
                .filter(carService -> carService.getCarsWaitingForService().size() > 0)
                .forEach(carService -> {
                    Optional<CarServiceSpot> spot = carService.getAvailableServiceSpot(false);
                    spot.ifPresent(carServiceSpot -> {
                        Vehicle vehicle = carService.getCarsWaitingForService().poll();
                        spot.get().putVehicle(vehicle);
                    });
                });

        dataStock.getCarServicesWithOwners().keySet()
                .stream()
                .filter(carService -> carService.getCarsWaitingForIndependentSpot().size() > 0)
                .forEach(carService -> {
                    Optional<CarServiceSpot> spot = carService.getAvailableServiceSpot(true);
                    spot.ifPresent(carServiceSpot -> {
                        Vehicle vehicle = carService.getCarsWaitingForIndependentSpot().poll();
                        carServiceSpot.putVehicle(vehicle);
                    });
                });
    }

    public void addHistoryEntry(ServiceHistoryEntry entry) {
        dataStock.addServiceHistoryEntry(entry);
    }
}
