package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.person.CarServiceOwner;
import pl.mihome.s23419p01.model.rent.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OwnerMenu extends MenuClass {

    public OwnerMenu() {
        this.title = "owner menu";
    }

    @Override
    void printMenuDetails() {
        System.out.println("1. My profile");
        System.out.println("2. Available Warehouses");
        System.out.println("3. Available Car Park spaces");
        System.out.println("4. Add Consumer Warehouse");
        System.out.println("5. Add Parking Space");
        System.out.println("6. Add Car Service Spot");
        System.out.println("7. Add Independent Car Service Spot");
        System.out.println();
        System.out.println("8. Add new Car Service");
        System.out.println();
        System.out.println("9. Export Data to File");
        possibleChoices.addAll(IntStream.range(1, 11).boxed().collect(Collectors.toSet()));
        chosen = pickYourNumber(10);
    }


    @Override
    Menu handleChoice(int choice) {
        switch (choice) {
            case 1:
                printTitle("user info");
                System.out.println(dataStock.getCurrentUser().toString());
                System.out.println();
                System.out.println("Press ENTER to continue...");
                scanner.nextLine();
                scanner.nextLine();
                return new OwnerMenu();
            case 2:
                return new WarehousesMenu();
            case 3:
                return new WarehousesMenu(true);
            case 4:
                try {
                    newWarehouse(false);
                }
                catch (IllegalStateException ex) {
                    System.out.println(ex.getMessage());
                }
                return new OwnerMenu();
            case 5:
                try {
                    newWarehouse(true);
                }
                catch (IllegalStateException ex) {
                    System.out.println(ex.getMessage());
                }
                return new OwnerMenu();
            case 6:
                try {
                    newServiceSpot(false);
                }
                catch (IllegalStateException ex) {
                    System.out.println(ex.getMessage());
                }
                return new OwnerMenu();
            case 7:
                try {
                    newServiceSpot(true);
                }
                catch (IllegalStateException ex) {
                    System.out.println(ex.getMessage());
                }
                return new OwnerMenu();
            case 8:
                newCarService();
                return new OwnerMenu();
            case 10:
                System.out.println();
                System.out.println("Wylogowano");
                System.out.println("*********************");
                System.out.println();
                return new UserMenu();
            default:
                return new OwnerMenu();
        }
    }

    private void newCarService() {
        String name;
        BigDecimal areaSquareMeters;
        BigDecimal heightMeters;
        BigDecimal ownStockPercent;
        printTitle("new car service");

        System.out.print("Car Service Name: ");
        name = scanner.next();

        System.out.print("Area (m2): ");
        while (!scanner.hasNextBigDecimal()) {
            System.out.print("Try again: ");
            scanner.next();
        }
        areaSquareMeters = scanner.nextBigDecimal();

        System.out.print("Height (m): ");
        while (!scanner.hasNextBigDecimal()) {
            System.out.print("Try again: ");
            scanner.next();
        }
        heightMeters = scanner.nextBigDecimal();

        System.out.print("Own stock percentage (%): ");
        while (!scanner.hasNextBigDecimal()) {
            System.out.print("Try again: ");
            scanner.next();
        }
        ownStockPercent = scanner.nextBigDecimal();

        try {
        CarService added = new CarService(name, ownStockPercent, areaSquareMeters, heightMeters);
            dataStock.addCarService(added, (CarServiceOwner) dataStock.getCurrentUser());
            System.out.println("New car service added - " + added.toString());
        }
        catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void newWarehouse(boolean isParkSpace) throws IllegalStateException {
        CarService chosenCarService = chooseCarService();
        BigDecimal pricePerDay;
        String declaration = "";

        while (!declaration.equals("S") && !declaration.equals("D")) {
            System.out.print("How do you want to input space size (S - space in m3, D - three dimensions): ");
            declaration = scanner.next();
        }

        System.out.print("PLN price per day: ");
        while (!scanner.hasNextBigDecimal()) {
            System.out.print("Try again: ");
            scanner.next();
        }
        pricePerDay = scanner.nextBigDecimal();

        RentableArea rentable;


        if (declaration.equals("S")) {
            System.out.print("Space size (m3): ");
            while (!scanner.hasNextBigDecimal()) {
                System.out.print("Try again: ");
                scanner.next();
            }
            BigDecimal space = scanner.nextBigDecimal();
            if (isParkSpace) {
                rentable = new ParkingSpace(space, pricePerDay);
            } else {
                rentable = new ConsumerWarehouse(space, pricePerDay);
            }
        } else {
            System.out.print("Length (m): ");
            while (!scanner.hasNextBigDecimal()) {
                System.out.print("Try again: ");
                scanner.next();
            }
            BigDecimal l = scanner.nextBigDecimal();
            System.out.print("Width (m): ");
            while (!scanner.hasNextBigDecimal()) {
                System.out.print("Try again: ");
                scanner.next();
            }
            BigDecimal w = scanner.nextBigDecimal();
            System.out.print("Height (m3): ");
            while (!scanner.hasNextBigDecimal()) {
                System.out.print("Try again: ");
                scanner.next();
            }
            BigDecimal h = scanner.nextBigDecimal();

            if (isParkSpace) {
                rentable = new ParkingSpace(l, w, h, pricePerDay);
            } else {
                rentable = new ConsumerWarehouse(l, w, h, pricePerDay);
            }
        }

        try {
            chosenCarService.addWarehouse(rentable);
            System.out.println("New renatable added - " + rentable.toString());
        }
        catch (IllegalStateException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void newServiceSpot(boolean isIndependent) throws IllegalStateException {
        CarService chosenCarService = chooseCarService();
        BigDecimal pricePerDay;

        System.out.print("PLN price per service daily: ");
        while (!scanner.hasNextBigDecimal()) {
            System.out.print("Try again: ");
            scanner.next();
        }
        pricePerDay = scanner.nextBigDecimal();

        CarServiceSpot spot;

        if (isIndependent) {
            spot = new IndependentCarServiceSpot(pricePerDay, chosenCarService);
        } else {
            spot = new CarServiceSpot(pricePerDay, chosenCarService);
        }

        try {
            chosenCarService.addServiceSpot(spot);
            System.out.println("New service spot added - " + spot.toString());
        }
        catch (IllegalStateException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private CarService chooseCarService() {
        List<CarService> carServices = dataStock.getCarServicesWithOwners().entrySet().stream()
                .filter(entry -> entry.getValue().equals(dataStock.getCurrentUser()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        CarService pickedCarService;
        if(carServices.isEmpty()) {
            throw new IllegalStateException("No car services available for this owner");
        }
        if (carServices.size() == 1) {
            pickedCarService = carServices.get(0);
        } else {
            printTitle("choose car service to add rentable");
            for (int i = 1; i <= carServices.size(); i++) {
                System.out.println(i + ". " + carServices.get(i - 1));
            }
            int carServiceChoice = 0;
            while (carServiceChoice < 1 || carServiceChoice > carServices.size()) {
                System.out.print("Which one? Make your choice: ");
                if (scanner.hasNextInt()) {
                    carServiceChoice = scanner.nextInt();
                }
            }
            pickedCarService = carServices.get(carServiceChoice - 1);
        }
        return pickedCarService;
    }

}
