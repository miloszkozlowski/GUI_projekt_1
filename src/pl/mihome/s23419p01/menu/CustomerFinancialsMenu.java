package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.BookEntry;
import pl.mihome.s23419p01.service.BookService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomerFinancialsMenu extends MenuClass {

    Map<Integer, BookEntry> payments = new HashMap<>();
    int exit;

    public CustomerFinancialsMenu() {
        this.title = "Financials";
    }

    @Override
    void printMenuDetails() {
        List<BookEntry> entries = dataStock.getCurrentUser().getPayments().stream()
                .sorted(Comparator.comparing(BookEntry::getPaymentDeadline))
                .collect(Collectors.toList());
        System.out.println("Pick payment to pay now:");
        System.out.println();
        int i = 1;
        for(BookEntry entry: entries) {
            payments.put(i, entry);
            System.out.println(i++ + ". " + entry.toString());
        }
        possibleChoices.addAll(IntStream.range(1, i).boxed().collect(Collectors.toSet()));
        exit = i;
        pickYourNumber(i);
    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == exit) {
            return new CustomerMenu();
        }
        try {
            BookService bs = new BookService();
            bs.settleThePaymentById(payments.get(choice).getId());
        }
        catch (IllegalStateException ex) {
            System.out.println();
            System.out.println(ex.getMessage());
        }
        return new CustomerFinancialsMenu();
    }
}
