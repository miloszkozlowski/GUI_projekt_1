package pl.mihome.s23419p01.service;

import pl.mihome.s23419p01.model.BookEntry;
import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.CustomerProperty;
import pl.mihome.s23419p01.model.ServiceHistoryEntry;
import pl.mihome.s23419p01.model.rent.CarServiceSpot;
import pl.mihome.s23419p01.model.rent.ParkingSpace;
import pl.mihome.s23419p01.model.rent.RentableArea;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {

    private final String WAREHOUSES_FILENAME = "warehouses.txt";
    private final String SERVICES_FILENAME = "services.txt";

    List<CarService> carServices;

    public FileService(List<CarService> carServices) {
        this.carServices = carServices;
    }

    public void saveDataToFiles() {
        saveWarehouses();
        saveServices();
    }

    private void saveWarehouses() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(WAREHOUSES_FILENAME))) {
            for (CarService carService : carServices) {
                List<RentableArea> rentables = carService.getWarehousesSet()
                        .stream()
                        .sorted(Comparator.comparing(RentableArea::getAreaCubicMeters))
                        .collect(Collectors.toList());

                br.write(carService.toString() + "\n");
                for (RentableArea r : rentables) {
                    br.write(r.toString() + "\n");
                    if (r instanceof ParkingSpace) {
                        br.write(((ParkingSpace) r).getVehicleParked() == null ? "No vehicle parked \n" : ((ParkingSpace) r).getVehicleParked().toString() + "\n");
                    } else {
                        List<CustomerProperty> properties = r.getProperties().stream()
                                .sorted(Comparator.comparing(CustomerProperty::getSpaceCubicMeters).reversed())
                                .collect(Collectors.toList());
                        for (CustomerProperty property : properties) {
                            br.write(property.toString() + "\n");
                        }
                    }
                }
            }
            System.out.println("Rentables data was exported to " + WAREHOUSES_FILENAME);
        } catch (IOException ex) {
            System.out.println("Rentables export was not possible at this time because: " + ex.getMessage());
        }

    }

    private void saveServices() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(SERVICES_FILENAME))) {
            for (CarService carService : carServices) {
                br.write(carService.toString() + "\n");
                for (CarServiceSpot spot : carService.getServiceSpots()) {
                    br.write(spot.toString() + "\n");
                    br.write(spot.isAvailable() ? "Not occupied\n" : spot.getVehicleOn().toString() + "\n");
                    for(ServiceHistoryEntry entry: spot.getServiceHistory()) {
                        br.write(entry.toString() + "\n");
                    }
                }
            }
            System.out.println("Services data was exported to " + WAREHOUSES_FILENAME);
        } catch (IOException ex) {
            System.out.println("Services export was not possible at this time because: " + ex.getMessage());
        }
    }
}
