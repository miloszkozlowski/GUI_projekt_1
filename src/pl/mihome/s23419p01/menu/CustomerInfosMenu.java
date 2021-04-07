package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.info.Info;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerInfosMenu extends MenuClass {

    public CustomerInfosMenu() {
        this.title = "notifications";
    }

    @Override
    void printMenuDetails() {
        List<Info> entries = dataStock.getCurrentUser().getAlerts().stream()
                .sorted(Comparator.comparing(Info::getInfoDate))
                .collect(Collectors.toList());
        for(Info entry: entries) {
            System.out.println(entry.toString());
        }
        pickYourNumber(9);
    }

    @Override
    Menu handleChoice(int choice) {
        return new CustomerMenu();
    }
}
