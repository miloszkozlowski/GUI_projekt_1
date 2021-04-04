package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.rent.Rentable;

public class WarehouseDetailMenu extends MenuClass {

    Rentable rentable;


    public WarehouseDetailMenu(Rentable rentable) {
        super();
        this.rentable = rentable;
        this.title = rentable.toString();
    }

    @Override
    void printMenuDetails() {
        System.out.print("Status: ");
        System.out.println(rentable.isAvailableForRent() ? "ready to be rented" : "occupied");
        if(rentable.isAvailableForRent()) {
            System.out.println("1. Start rent");
            possibleChoices.add(1);
        }
        chosen = pickYourNumber(9);
    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == 9) {
            return new WarehousesMenu();
        }
        else {
            return new RentRentableMenu(rentable);
        }
    }

}
