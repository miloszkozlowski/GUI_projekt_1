package pl.mihome.s23419p01.model.person;

import pl.mihome.s23419p01.model.CarService;

import java.util.HashSet;
import java.util.Set;

public class CarServiceOwner extends Person {

    Set<CarService> carServicesSet = new HashSet<>();

    public CarServiceOwner(String firstName, String lastName, String pesel, String residenceAddress) {
        super(firstName, lastName, pesel, residenceAddress);
    }
}
