package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.rent.RentableArea;

import java.util.HashMap;
import java.util.Map;

public class UsersApprovedMenu extends MenuClass {

    RentableArea rentable;
    int addMode;

    public UsersApprovedMenu(RentableArea rentable) {
        this.rentable = rentable;
        this.title = "Users approved to manage " + rentable.toString();
    }

    @Override
    void printMenuDetails() {
        System.out.println("Choose person to remove permission:");
        System.out.println();
        int x = 1;
        for(Person p: rentable.getApprovedToEnter()) {
            if(!p.equals(dataStock.getCurrentUser())) {
                System.out.println(x + ". " + p.toString());
                possibleChoices.add(x++);
            }
        }
        System.out.println();
        System.out.println(x + ". Add Approved Person");
        possibleChoices.add(x);
        addMode = x;
        pickYourNumber(x+1);
    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == addMode) {
            Map<Integer, Person> userChoices = new HashMap<>();
            int i = 1;
            for(Person p: dataStock.getPeople()) {
                if(!rentable.getApprovedToEnter().contains(p) && !p.equals(dataStock.getCurrentUser())) {
                    System.out.println(i + ". " + p.toString());
                    userChoices.put(i++, p);
                }
            }
            System.out.println();
            System.out.println(i + ". Go back");
            System.out.println();
            System.out.print("Pick your number: ");
            int userChosen = -1;
            while(!userChoices.containsKey(userChosen) && userChosen!=i) {
                if(scanner.hasNextInt()) {
                    userChosen = scanner.nextInt();
                }
                scanner.nextLine();
            }
            if(userChosen != i) {
                rentable.manageApprovedToEnter(userChoices.get(userChosen), true);
            }

        }
        else if(choice != addMode+1) {
            rentable.manageApprovedToEnter(rentable.getApprovedToEnter().get(choice), false);
        }
        else {
            return new CustomerRentableDetailMenu(rentable);
        }
        return new UsersApprovedMenu(rentable);
    }
}
