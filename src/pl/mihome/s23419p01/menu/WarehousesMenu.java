package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.rent.ConsumerWarehouse;
import pl.mihome.s23419p01.model.rent.ParkingSpace;
import pl.mihome.s23419p01.model.rent.Rentable;
import pl.mihome.s23419p01.model.rent.RentableArea;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WarehousesMenu extends MenuClass {

    Map<Integer, Rentable> choices = new HashMap<>();
    boolean carParkRental = false;

    public WarehousesMenu() {
        super();
        this.title = "Available Warehouses";
    }

    public WarehousesMenu(boolean carParkRentals) {
        this();
        this.carParkRental = carParkRentals;
        this.title = "Available Car Park Spaces";
    }


    @Override
    void printMenuDetails() {
        int i = 1;
        if(carParkRental) {
            for (Map.Entry<ParkingSpace, CarService> entry : getAvailableParkSpacesMap().entrySet()) {
                System.out.println(i + ". " + entry.getKey().toString() + " (" + entry.getValue().toString() + ")");
                choices.put(i, entry.getKey());
                possibleChoices.add(i++);
            }
        }
        else {
            for (Map.Entry<ConsumerWarehouse, CarService> entry : getAvailableWarehousesMap().entrySet()) {
                System.out.println(i + ". " + entry.getKey().toString() + " (" + entry.getValue().toString() + ")");
                choices.put(i, entry.getKey());
                possibleChoices.add(i++);
            }
        }
        pickYourNumber(i);
    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == possibleChoices.size()-1) {
            return new OwnerMenu();
        }
        else {
            return new WarehouseDetailMenu(choices.get(choice));
        }

    }


    private Map<ConsumerWarehouse, CarService> getAvailableWarehousesMap() {
        Map<CarService, List<ConsumerWarehouse>> mapToProcess = dataStock.getCarServicesWithOwners().entrySet().stream()
                .filter(entry -> entry.getValue().equals(dataStock.getCurrentUser()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toMap(Function.identity(), CarService::getListOfAvailableConsumerWarehouses));

        Map<ConsumerWarehouse, CarService> toReturn = new LinkedHashMap<>();

        for(Map.Entry<CarService, List<ConsumerWarehouse>> element: mapToProcess.entrySet()) {
            for(ConsumerWarehouse cw: element.getValue()) {
                toReturn.put(cw, element.getKey());
            }
        }
        return toReturn;
    }

    private Map<ParkingSpace, CarService> getAvailableParkSpacesMap() {
        Map<CarService, List<ParkingSpace>> mapToProcess = dataStock.getCarServicesWithOwners().entrySet().stream()
                .filter(entry -> entry.getValue().equals(dataStock.getCurrentUser()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toMap(Function.identity(), CarService::getListOfAvailableParkingSpaces));

        Map<ParkingSpace, CarService> toReturn = new LinkedHashMap<>();

        for(Map.Entry<CarService, List<ParkingSpace>> element: mapToProcess.entrySet()) {
            for(ParkingSpace cw: element.getValue()) {
                toReturn.put(cw, element.getKey());
            }
        }
        return toReturn;
    }


}
