package pl.mihome.s23419p01.service;

import pl.mihome.s23419p01.model.BookEntry;
import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.person.CarServiceOwner;
import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.rent.CarServiceSpot;
import pl.mihome.s23419p01.model.rent.RentableArea;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Crawler {

    private final DataStock dataStock;

    public Crawler() {
        this.dataStock = DataStock.getInstance();
    }

    public void crawl() {
        checkOutRentables(dataStock.getCarServicesWithOwners().keySet());
        checkFinancialBook(dataStock.getPaymentsBook(), dataStock.getCurrentDate());
    }

    private void checkOutRentables(Set<CarService> carServices) {
        System.out.println("Start check...");
        carServices.stream()
                .flatMap(service -> service.getWarehousesSet().stream())
                .filter(rentable -> !rentable.isAvailableForRent())
                .forEach(RentableArea::refreshCurrentState);
        System.out.println("Checking finished");
    }

    private void checkFinancialBook(Set<BookEntry> bookEntries, LocalDate currentDate) {
        System.out.println("Start second check...");
        bookEntries.stream()
                .filter(bookEntry -> currentDate.isAfter(bookEntry.getPaymentDeadline()) && !bookEntry.isPaid() && !bookEntry.isNotified())
                .forEach(BookEntry::notifyDebtor);
        System.out.println("Finished second check...");

    }


}
