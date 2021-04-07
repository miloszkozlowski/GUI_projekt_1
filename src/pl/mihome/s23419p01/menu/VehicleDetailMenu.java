package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.rent.CarServiceSpot;
import pl.mihome.s23419p01.model.vehicle.Vehicle;

import java.util.*;

public class VehicleDetailMenu extends MenuClass {

    private final Vehicle vehicle;

    public VehicleDetailMenu(Vehicle vehicle) {
        this.title = vehicle.toString();
        this.vehicle = vehicle;
    }

    @Override
    void printMenuDetails() {
        Optional<CarServiceSpot> serviceSpot = vehicle.getVehicleServiceSpot();
        if(serviceSpot.isEmpty()) {
            System.out.println("1. Take the vehicle to the site");
            possibleChoices.add(1);
        }
        else {
            System.out.println("Vehicle is being currently serviced at " + serviceSpot.get().toString());
            System.out.println();
            System.out.println("2. Remove car from service spot");
            possibleChoices.add(2);
        }
        pickYourNumber(9);
    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == 9) {
            return new VehiclesMenu();
        }
        if(choice == 1) {
            System.out.println();
            System.out.println("Where do you want to put your vehicle?");
            System.out.println();
            List<CarService> carServices = new ArrayList<>(dataStock.getCarServicesWithOwners().keySet());
            for (int i = 0; i < carServices.size(); i++) {
                System.out.println(i + 1 + ". " + carServices.get(i).toString());
            }
            System.out.print("Pick type of choice: ");
            int carServiceChoice = 0;
            while (carServiceChoice <= 0 || carServiceChoice > carServices.size()) {
                if (scanner.hasNextInt()) {
                    carServiceChoice = scanner.nextInt();
                }
                scanner.nextLine();
            }
            CarService chosenService = carServices.get(carServiceChoice - 1);
            System.out.println();
            System.out.println("What type of spot do you prefer?");
            System.out.println();
            System.out.println("1. Professional Service Spot");
            System.out.println("2. Independent Service Spot");
            System.out.println("9. Go back");
            int typeChoice = 0;
            while (typeChoice != 1 && typeChoice != 2 && typeChoice != 9) {
                if (scanner.hasNextInt()) {
                    typeChoice = scanner.nextInt();
                }
                scanner.nextLine();
            }
            if (typeChoice == 9) {
                return new VehicleDetailMenu(vehicle);
            }
            boolean independent = typeChoice == 2;

            CarServiceSpot assignedSpot = chosenService.serviceCar(vehicle, independent);
            if (assignedSpot == null) {
                System.out.println("No available spots at this service. Your vehicle was added to waiting line.");
            } else {
                System.out.println("Your vehicle has been assigned to " + assignedSpot.toString());
            }
        }
        else if(choice==2) {
            Optional<CarServiceSpot> serviceSpot = vehicle.getVehicleServiceSpot();
            serviceSpot.ifPresent(CarServiceSpot::removeVehicle);
            System.out.println();
            System.out.println("The vehicle has been removed from the service spot.");
        }

        return new VehiclesMenu();
    }
}
