package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.vehicle.Vehicle;

import java.util.Arrays;

public class CustomerMenu extends MenuClass implements Menu {

    public CustomerMenu() {
        super();
        this.title = "user menu";
    }

    @Override
    void printMenuDetails() {
        System.out.println("1. My Profile");
        System.out.println("2. My Vehicles");
        System.out.println("3. New vehicle");
        System.out.println("4. My Warehouses");
        System.out.println("5. My Parking spaces");
        System.out.println("6. Notifications");
        System.out.println("7. Financials");
        possibleChoices.addAll(Arrays.asList(1,2,3,4,5,6,7,9));
        pickYourNumber(9);
    }

    @Override
    Menu handleChoice(int choice) {
        switch (choice) {
            case 1:
                printTitle("user info");
                System.out.println(dataStock.getCurrentUser().toString());
                System.out.println();
                System.out.println(" Current rentals - Warehouses & Parking Spaces:");
                System.out.println("================================================");
                System.out.println();
                dataStock.getCarServicesWithOwners().keySet().stream()
                        .flatMap(carService -> carService.getWarehousesSet().stream())
                        .filter(rentable -> {
                            Person person = rentable.whoIsPaying();
                            if(person == null) {
                                return false;
                            }
                            return person.equals(dataStock.getCurrentUser());
                        })
                        .forEach(System.out::println);
                System.out.println();
                System.out.println(" Services:");
                System.out.println("===========");
                System.out.println();
                dataStock.getCarServicesWithOwners().keySet().stream()
                        .flatMap(carService -> carService.getServiceSpots().stream())
                        .filter(spot -> {
                            Vehicle vehicleOn = spot.getVehicleOn();
                            if(vehicleOn == null) {
                                return false;
                            }
                            return vehicleOn.getOwner().equals(dataStock.getCurrentUser());
                        })
                        .forEach(System.out::println);
                System.out.println();
                System.out.println("Press ENTER to continue...");
                scanner.nextLine();
                scanner.nextLine();
                return new CustomerMenu();
            case 2:
                return new VehiclesMenu();
            case 3:
                return new NewVehicleMenu();
            case 4:
                return new CustomerRentablesMenu(false);
            case 5:
                return new CustomerRentablesMenu(true);
            case 6:
                return new CustomerInfosMenu();
            case 7:
                return new CustomerFinancialsMenu();
            case 9:
                return new UserMenu();
        }
        return null;
    }
}
