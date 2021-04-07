package pl.mihome.s23419p01.service;

import pl.mihome.s23419p01.model.BookEntry;
import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.rent.RentableArea;

import java.time.LocalDate;
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
        carServices.stream()
                .flatMap(service -> service.getWarehousesSet().stream())
                .filter(rentable -> !rentable.isAvailableForRent())
                .forEach(RentableArea::refreshCurrentState);
    }

    private void checkFinancialBook(Set<BookEntry> bookEntries, LocalDate currentDate) {
        bookEntries.stream()
                .filter(bookEntry -> currentDate.isAfter(bookEntry.getPaymentDeadline()) && !bookEntry.isPaid() && !bookEntry.isNotified())
                .forEach(BookEntry::notifyDebtor);

    }




}
