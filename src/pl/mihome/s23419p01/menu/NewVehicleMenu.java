package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.vehicle.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class NewVehicleMenu extends MenuClass{

    private Vehicle vehicle;

    public NewVehicleMenu() {
        this.title = "your new vehicle";
    }

    @Override
    void printMenuDetails() {
        System.out.println("1. Amphibian");
        System.out.println("2. City Car");
        System.out.println("3. Off Road Car");
        System.out.println("4. Motorcycle");
        possibleChoices.addAll(Arrays.asList(1,2,3,4));
        pickYourNumber(9);
    }


    @Override
    Menu handleChoice(int choice) {
        if(choice == 9) {
            return new CustomerMenu();
        }
        System.out.println("Input vehicle data.");
        System.out.println();
        String vehicleName = "";
        BigDecimal sizeCubicMeters = BigDecimal.ZERO;
        System.out.print("Name: ");
        while(vehicleName.isBlank()) {
            vehicleName = scanner.nextLine();
        }
        System.out.print("Size (m3): ");
        while(sizeCubicMeters.compareTo(BigDecimal.ZERO) <= 0) {
            if(scanner.hasNextBigDecimal()) {
                sizeCubicMeters = scanner.nextBigDecimal();
            }
            scanner.nextLine();
        }

        switch (choice) {
            case 1:
                String sideColor = "";
                System.out.print("Side color: ");
                while (sideColor.isBlank()) {
                    sideColor = scanner.nextLine();
                }
                vehicle = new Amphibian(vehicleName, sizeCubicMeters, sideColor);
                break;
            case 2:
            case 3:
                String towbar = "";
                System.out.print("Does the car have towabar (Y/N)? ");
                while(!towbar.equals("Y") && !towbar.equals("N")) {
                    towbar = scanner.nextLine();
                }
                boolean doesHaveTowbar = towbar.equals("Y");
                if(choice == 2) {
                    int amountOfIsoFixSeats = -1;
                    System.out.print("How many ISO-FIXes does the car have (0-4)? ");
                    while (amountOfIsoFixSeats < 0 || amountOfIsoFixSeats > 4) {
                        if(scanner.hasNextInt()) {
                            amountOfIsoFixSeats = scanner.nextInt();
                        }
                        scanner.nextLine();
                    }
                    vehicle = new CityCar(vehicleName, sizeCubicMeters, doesHaveTowbar, amountOfIsoFixSeats);
                }
                else {
                    int amountOfAdditionalLights = -1;
                    System.out.print("How many additional lights does the car have (0-20)? ");
                    while (amountOfAdditionalLights < 0 || amountOfAdditionalLights > 20) {
                        if(scanner.hasNextInt()) {
                            amountOfAdditionalLights = scanner.nextInt();
                        }
                        scanner.nextLine();
                    }
                    vehicle = new OffRoadCar(vehicleName, sizeCubicMeters, doesHaveTowbar, amountOfAdditionalLights);
                }
                break;
            case 4:
                String sideTrunks = "";
                System.out.print("Does the motorcycle have side trunks (Y/N)? ");
                while(!sideTrunks.equals("Y") && !sideTrunks.equals("N")) {
                    sideTrunks = scanner.nextLine();
                }
                boolean doesHaveSideTrunks = sideTrunks.equals("Y");
                vehicle = new Motorcycle(vehicleName, sizeCubicMeters, doesHaveSideTrunks);
                break;
        }
        dataStock.getCurrentUser().addVehicle(vehicle);
        return new VehiclesMenu();
    }
}
