package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.exception.TooManyThingsException;
import pl.mihome.s23419p01.model.CustomerProperty;
import pl.mihome.s23419p01.model.rent.ConsumerWarehouse;
import pl.mihome.s23419p01.model.rent.ParkingSpace;
import pl.mihome.s23419p01.model.rent.RentableArea;
import pl.mihome.s23419p01.model.vehicle.Vehicle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomerRentableDetailMenu extends MenuClass {

    RentableArea rentableArea;
    List<CustomerProperty> properties;
    int exitNumber;
    Map<Integer, CustomerProperty> propertyMap = new HashMap<>();


    public CustomerRentableDetailMenu(RentableArea rentableArea) {
        this.rentableArea = rentableArea;
        this.title = rentableArea.toString();
        this.properties = new ArrayList<>(rentableArea.getProperties());
    }

    @Override
    void printMenuDetails() {
        if (rentableArea instanceof ConsumerWarehouse) {
            System.out.println("Items inside (pick number to remove an item):");
            System.out.println();
            for (int i = 0; i < properties.size(); i++) {
                System.out.println(i + 1 + ". " + properties.get(i).getName() + " (size: " + properties.get(i).getSpaceCubicMeters() + ")");
                propertyMap.put(i+1, properties.get(i));
            }
            System.out.println();
            System.out.println(properties.size() + 1 + ". Insert new item");
            if(rentableArea.whoIsPaying()!= null) {
                if(rentableArea.whoIsPaying().equals(dataStock.getCurrentUser())) {
                    System.out.println(properties.size() + 2 + ". Users approved");
                }
            }
            exitNumber = properties.size() + 3;
            if (!properties.isEmpty()) {
                possibleChoices.addAll(IntStream.range(1, properties.size() + 3).boxed().collect(Collectors.toList()));
            }
            possibleChoices.add(properties.size() + 1);
        } else if (rentableArea instanceof ParkingSpace) {
            ParkingSpace parkingSpace = (ParkingSpace) rentableArea;
            if (parkingSpace.isCarParked()) {
                System.out.println("There is a vehicle parked on this parking space: " + parkingSpace.getVehicleParked().toString());
                System.out.println();
                System.out.println("1. Remove the car from parking space");
                possibleChoices.add(1);
            } else {
                System.out.println("2. Park your car here");
                possibleChoices.add(2);
            }
            exitNumber = 9;
        }
        pickYourNumber(exitNumber);

    }

    @Override
    Menu handleChoice(int choice) {
        if (choice == exitNumber) {
            return new CustomerRentablesMenu(rentableArea instanceof ParkingSpace);
        }
        if (choice == properties.size()+2) {
            return new UsersApprovedMenu(rentableArea);
        }
        if (rentableArea instanceof ConsumerWarehouse) {
            if (choice == properties.size() + 2) {
                return new UsersApprovedMenu(rentableArea);
            }
            else if (choice == properties.size() + 1) {
                System.out.println();
                System.out.println(" Insert new item");
                System.out.println("==================");
                System.out.println();
                String itemName = "";
                System.out.print("Name: ");
                while (itemName.isBlank()) {
                    itemName = scanner.nextLine();
                }
                System.out.print("Size (m3): ");
                BigDecimal itemSize = BigDecimal.ZERO;
                while (itemSize.compareTo(BigDecimal.ZERO) <= 0) {
                    if (scanner.hasNextBigDecimal()) {
                        itemSize = scanner.nextBigDecimal();
                    }
                    scanner.nextLine();
                }
                CustomerProperty item = new CustomerProperty(itemSize, itemName);
                try {
                    ((ConsumerWarehouse) rentableArea).insertItem(item);
                    System.out.println();
                    System.out.println(item.getName() + " has been inserted to " + rentableArea.toString());
                } catch (TooManyThingsException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                CustomerProperty item = propertyMap.get(choice);
                ((ConsumerWarehouse) rentableArea).removeItem(item.getId());
                System.out.println();
                System.out.println(item.getName() + " has been removed from " + rentableArea.toString());
            }
        } else {
            if (choice == 1) {
                ((ParkingSpace) rentableArea).moveTheCarOut();
                System.out.println();
                System.out.println("The vehicle has been moved out from your parking space.");
            } else {
                System.out.println();
                System.out.println();
                List<Vehicle> vehicles = dataStock.getCurrentUser().getVehicles();
                for (int i = 0; i < vehicles.size(); i++) {
                    System.out.println(i + 1 + ". " + vehicles.get(i).toString());
                }
                if (vehicles.isEmpty()) {
                    System.out.println("You do not have any vehicles baby... :(");
                } else {
                    System.out.print("Pick the vehicle: ");
                    int vehicleOfChoice = 0;
                    while (vehicleOfChoice <= 0) {
                        if (scanner.hasNextInt()) {
                            vehicleOfChoice = scanner.nextInt();
                        }
                        scanner.nextLine();
                    }
                    ((ParkingSpace) rentableArea).moveTheCarIn(vehicles.get(vehicleOfChoice - 1));
                    System.out.println();
                    System.out.println(vehicles.get(vehicleOfChoice - 1).toString() + " has been parked on " + rentableArea.toString());
                }
            }
        }
        return new CustomerRentableDetailMenu(rentableArea);
    }
}
