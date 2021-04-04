package pl.mihome.s23419p01.service;

import pl.mihome.s23419p01.model.BookEntry;
import pl.mihome.s23419p01.model.info.Info;
import pl.mihome.s23419p01.model.info.TenantAlert;
import pl.mihome.s23419p01.model.person.Person;

import java.util.*;
import java.util.stream.Collectors;

public class BookService {

    private final Map<UUID, BookEntry> bookEntries = new HashMap<>();
    private final Person currentUser;

    public BookService() {
        DataStock dataStock = DataStock.getInstance();
        dataStock.getPaymentsBook()
                .forEach(bookEntry -> this.bookEntries.put(bookEntry.getId(), bookEntry));
        this.currentUser = dataStock.getCurrentUser();
    }

    public void settleThePaymentById(UUID id) throws IllegalArgumentException, IllegalStateException {
        if(!bookEntries.containsKey(id)) {
            throw new IllegalArgumentException("No such entry in the book.");
        }
        BookEntry editedEntry = bookEntries.get(id);
        if(!editedEntry.getPersonId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Tou cannot pay for someone else. Are you charity or somethin'?");
        }
        if(editedEntry.isPaid()) {
            throw new IllegalStateException("This is already paid yo!");
        }

        editedEntry.settlePayment();

        Set<TenantAlert> toBeRemoved = currentUser.getAlerts().stream()
                .filter(info -> info instanceof TenantAlert)
                .map(info -> (TenantAlert)info)
                .filter(alert -> alert.getWarehouseId().equals(editedEntry.getRentableId()))
                .collect(Collectors.toSet());
        currentUser.removeAlerts(toBeRemoved);
    }
}
