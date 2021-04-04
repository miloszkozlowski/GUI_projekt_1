package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.ServiceHistoryEntry;
import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.vehicle.Vehicle;
import pl.mihome.s23419p01.service.CarServiceSpotsManager;
import pl.mihome.s23419p01.service.Crawler;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class CarServiceSpot {
    public static final BigDecimal SPACE_CUBIC_METERS = BigDecimal.valueOf(120);
    private Vehicle vehicleOn;
    private byte currentServiceLength;
    private final Set<ServiceHistoryEntry> serviceHistory = new HashSet<>();
    protected final BigDecimal dailyServiceCost;
    private final CarService carService;

    public CarServiceSpot(BigDecimal dailyServiceCost, CarService carService) {
        this.dailyServiceCost = dailyServiceCost;
        this.carService = carService;
        carService.addServiceSpot(this);
    }

    public boolean isAvailable() {
        return vehicleOn == null;
    }

    public void putVehicle(Vehicle vehicle, Person person) {
        if(isAvailable()) {
            vehicleOn = vehicle;
            Random random = new Random();
            currentServiceLength = (byte)(random.nextInt(5)+1);
            ServiceHistoryEntry entry = new ServiceHistoryEntry(dailyServiceCost.multiply(BigDecimal.valueOf(currentServiceLength)), carService, this, vehicle, person);
            new CarServiceSpotsManager().addHistoryEntry(entry);
        }
        else {
            throw new IllegalStateException("This spot is occupied at the moment");
        }
    }

    public Vehicle removeVehicle() {
        if(isAvailable()) {
            throw new IllegalStateException("There is no vehicle here");
        }
        Vehicle v = vehicleOn;
        vehicleOn = null;
        currentServiceLength = 0;
        new CarServiceSpotsManager().refresh();
        return v;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(this instanceof IndependentCarServiceSpot) {
            stringBuilder.append("Independent ");
        }
        stringBuilder.append("Car Service Spot at ").append(carService.toString()).append(", daily fee PLN ").append(dailyServiceCost).append(", with vehicle on: ").append(vehicleOn);
        return stringBuilder.toString();
    }
}
