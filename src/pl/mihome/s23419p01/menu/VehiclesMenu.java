package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.vehicle.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class VehiclesMenu extends MenuClass {

    Map<Integer, Vehicle> vehicleChoices = new HashMap<>();
    int i = 1;

    public VehiclesMenu() {
        super();
        this.title = "My Vehicles";
    }

    @Override
    void printMenuDetails() {
        for(Vehicle v: dataStock.getCurrentUser().getVehicles()) {
            System.out.println(i + ". " + v.toString());
            vehicleChoices.put(i++, v);
        }
        possibleChoices.addAll(vehicleChoices.keySet());
        possibleChoices.add(i);
        pickYourNumber(i);
    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == i) {
            return new CustomerMenu();
        }
        return new VehicleDetailMenu(vehicleChoices.get(choice));
    }
}
