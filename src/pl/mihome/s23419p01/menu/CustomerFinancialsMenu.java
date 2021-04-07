package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.BookEntry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerFinancialsMenu extends MenuClass {

    public CustomerFinancialsMenu() {
        this.title = "Financials";
    }

    @Override
    void printMenuDetails() {
        List<BookEntry> entries = dataStock.getCurrentUser().getPayments().stream()
                .sorted(Comparator.comparing(BookEntry::getPaymentDeadline))
                .collect(Collectors.toList());
        for(BookEntry entry: entries) {
            System.out.println(entry.toString());
        }
        pickYourNumber(9);
    }

    @Override
    Menu handleChoice(int choice) {
        return new CustomerMenu();
    }
}
