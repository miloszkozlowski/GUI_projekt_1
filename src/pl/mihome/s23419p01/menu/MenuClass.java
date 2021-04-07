package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.service.DataStock;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public abstract class MenuClass implements Menu {

    DataStock dataStock;
    Scanner scanner = new Scanner(System.in);
    Set<Integer> possibleChoices = new HashSet<>();
    int chosen = -1;
    String title;

    public MenuClass() {
        this.dataStock = DataStock.getInstance();
        possibleChoices.add(0);
    }

    @Override
    public Menu display() {
        printTitle(title);
        while(!possibleChoices.contains(chosen)) {
            printMenuDetails();
        }
        if(chosen == 0) {
            return null;
        }
        return handleChoice(chosen);
    }

    abstract void printMenuDetails();

    abstract Menu handleChoice(int choice);

    void printTitle(String title) {
        System.out.println("****************************");
        System.out.println();
        System.out.println();
        System.out.println("  " + title.toUpperCase() + "   /today is: " + dataStock.getCurrentDateString() + "/");
        for(int i = 0; i<title.length()+4; i++) {
            System.out.print("^");
        }
        System.out.println();
    }

    void pickYourNumber(int exitNumber) {
        possibleChoices.add(exitNumber);
        int choice = -1;
        if(exitNumber != 0) {
            System.out.println();
            System.out.println();
            System.out.println(exitNumber + ". Go back");
        }
        System.out.println();
        System.out.println();
        System.out.println("Choose 0 and press enter to quit any time.");
        System.out.print("Pick your number: ");
        if(scanner.hasNextInt()) {
            choice = scanner.nextInt();
            if(possibleChoices.contains(choice)) {
                chosen = choice;
                return;
            }
        }

        System.out.println();
        System.out.println("Wrong choice. Try again.");
        System.out.println("************************");
        System.out.println();
        scanner.nextLine();

    }

}
