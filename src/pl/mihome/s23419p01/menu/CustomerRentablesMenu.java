package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.rent.ConsumerWarehouse;
import pl.mihome.s23419p01.model.rent.ParkingSpace;
import pl.mihome.s23419p01.model.rent.RentableArea;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomerRentablesMenu extends MenuClass {

    private final boolean parkingSpace;
    private int exitChoice;
    private List<RentableArea> rentables;
    private List<RentableArea> rentablesApproved;


    public CustomerRentablesMenu(boolean parkingSpace) {
        this.parkingSpace = parkingSpace;
        this.title = parkingSpace ? "Rented parking spaces" : "Rented Warehouses";
    }

    @Override
    void printMenuDetails() {
        if(parkingSpace) {
            rentables = dataStock.getCarServicesWithOwners().keySet().stream()
                    .flatMap(service -> service.getWarehousesSet().stream())
                    .filter(rentable -> rentable instanceof ParkingSpace)
                    .filter(rentable -> {
                        if(rentable.whoIsPaying() == null) {
                            return false;
                        }
                        else {
                            return rentable.whoIsPaying().equals(dataStock.getCurrentUser());
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            rentables = dataStock.getCarServicesWithOwners().keySet().stream()
                    .flatMap(service -> service.getWarehousesSet().stream())
                    .filter(rentable -> rentable instanceof ConsumerWarehouse)
                    .filter(rentable -> {
                        if(rentable.whoIsPaying() == null) {
                            return false;
                        }
                        else {
                            return rentable.whoIsPaying().equals(dataStock.getCurrentUser());
                        }
                    })
                    .collect(Collectors.toList());

            rentablesApproved = dataStock.getCarServicesWithOwners().keySet().stream()
                    .flatMap(service -> service.getWarehousesSet().stream())
                    .filter(rentable -> rentable instanceof ConsumerWarehouse)
                    .filter(rentable -> {
                        if(rentable.whoIsPaying() == null) {
                            return false;
                        }
                        else {
                            return !rentable.whoIsPaying().equals(dataStock.getCurrentUser()) && rentable.getApprovedToEnter().contains(dataStock.getCurrentUser());
                        }
                    })
                    .collect(Collectors.toList());
        }
        System.out.println("Rented by me:");
        System.out.println();
        for(int i = 0; i< rentables.size(); i++) {
            System.out.println(i+1 + ". " + rentables.get(i));
        }
        if(!rentablesApproved.isEmpty()) {
            System.out.println();
            System.out.println("Approved for use:");
            System.out.println();
            for(int i = 0; i<rentablesApproved.size(); i++) {
                System.out.println(i+1+ rentables.size() + ". " + rentablesApproved.get(i));
            }
        }
        possibleChoices = IntStream.range(1, rentables.size()+rentablesApproved.size()+1).boxed().collect(Collectors.toSet());
        exitChoice = rentables.size()+ rentablesApproved.size()+1;
        pickYourNumber(exitChoice);

    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == exitChoice) {
            return new CustomerMenu();
        }
        else {
            if(choice <= rentables.size()) {
                return new CustomerRentableDetailMenu(rentables.get(choice - 1));
            }
            else {
                return new CustomerRentableDetailMenu(rentablesApproved.get(rentables.size()-1+choice));
            }
        }
    }
}
