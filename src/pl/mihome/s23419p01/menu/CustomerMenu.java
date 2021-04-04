package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.service.DataStock;

import java.util.Scanner;

public class CustomerMenu extends MenuClass implements Menu {

    public CustomerMenu() {
        super();
        this.title = "user menu";
    }

    @Override
    void printMenuDetails() {
        chosen = pickYourNumber(9);
    }

    @Override
    Menu handleChoice(int choice) {
        return null;
    }
}
