package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.ServiceHistoryEntry;
import pl.mihome.s23419p01.model.vehicle.Vehicle;
import pl.mihome.s23419p01.service.CarServiceSpotsManager;
import pl.mihome.s23419p01.service.DataStock;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class CarServiceSpot {
    public static final BigDecimal SPACE_CUBIC_METERS = BigDecimal.valueOf(120);
    protected final BigDecimal dailyServiceCost;
    private final CarService carService;
    private Vehicle vehicleOn;
    private byte currentServiceLength;

    public CarServiceSpot(BigDecimal dailyServiceCost, CarService carService) {
        this.dailyServiceCost = dailyServiceCost;
        this.carService = carService;
        carService.addServiceSpot(this);
    }

    public Vehicle getVehicleOn() {
        return vehicleOn;
    }

    public List<ServiceHistoryEntry> getServiceHistory() {
        DataStock dataStock = DataStock.getInstance();
        return dataStock.getServicesHistory().stream()
                .filter(entry -> entry.getCarServiceSpot() == this)
                .sorted(Comparator.comparing(ServiceHistoryEntry::getServiceStartDate).reversed())
                .collect(Collectors.toList());
    }

    public boolean isAvailable() {
        return vehicleOn == null;
    }

    public void putVehicle(Vehicle vehicle) {
        if (isAvailable()) {
            vehicleOn = vehicle;
            Random random = new Random();
            currentServiceLength = (byte) (random.nextInt(5) + 1);
            DataStock dataStock = DataStock.getInstance();
            ServiceHistoryEntry entry = new ServiceHistoryEntry(dailyServiceCost.multiply(BigDecimal.valueOf(currentServiceLength)), this, vehicle, dataStock.getCurrentDate());
            new CarServiceSpotsManager().addHistoryEntry(entry);
        } else {
            throw new IllegalStateException("This spot is occupied at the moment");
        }
    }

    public Vehicle removeVehicle() {
        if (isAvailable()) {
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
        if (this instanceof IndependentCarServiceSpot) {
            stringBuilder.append("Independent ");
        }
        stringBuilder.append("Car Service Spot at ").append(carService.toString()).append(", daily fee PLN ").append(dailyServiceCost).append(", with vehicle on: ").append(vehicleOn);
        return stringBuilder.toString();
    }
}
