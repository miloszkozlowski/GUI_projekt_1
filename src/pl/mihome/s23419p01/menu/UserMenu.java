package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.person.CarServiceOwner;
import pl.mihome.s23419p01.model.person.Person;

import java.util.regex.Pattern;

public class UserMenu extends MenuClass {

    public UserMenu() {
        super();
        this.title = "Who are you?";
    }

    @Override
    void printMenuDetails() {
        dataStock.setCurrentUser(null);
        int i = 1;
        for(Person person: dataStock.getPeople()) {
            System.out.println(i + ". " + person.getName() + " (" + person.getId() + ")" + (person instanceof CarServiceOwner ? " - CAR SERVICE OWNER" : ""));
            possibleChoices.add(i++);
        }
        System.out.println();
        System.out.println(i + ". New Car Service Owner");
        possibleChoices.add(i++);
        System.out.println(i + ". New Customer");
        possibleChoices.add(i);
        pickYourNumber(0);
    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == possibleChoices.size()-1) {
            newUserScreen(false);
            return new UserMenu();
        }
        else if(choice == possibleChoices.size()-2) {
            newUserScreen(true);
            return new UserMenu();
        }
        else {
            dataStock.setCurrentUser(dataStock.getPeople().get(choice-1));
            if(dataStock.getCurrentUser() instanceof CarServiceOwner) {
                return new OwnerMenu();
            }
            else {
                return new CustomerMenu();
            }
        }

    }

    private void newUserScreen(boolean isOwner) {
        String firstName;
        String lastName;
        String pesel = "";
        Pattern peselPattern = Pattern.compile("^[0-9]{2}([02]{1}[1-9]{1}|[13]{1}[0-2]{1})([0-2]{1}[0-9]{1}|[3]{1}[0-1])[0-9]{5}$");
        String postalAddress;
        printTitle(isOwner ? "new owner" : "new customer");
        System.out.print("First name: ");
        firstName = scanner.next();
        System.out.print("Last name: ");
        lastName = scanner.next();
        while(!peselPattern.matcher(pesel).matches()) {
            System.out.print("PESEL: ");
            pesel = scanner.next();
        }
        System.out.print("Postal address: ");
        postalAddress = scanner.next();
        Person userAdded;
        if(isOwner) {
            userAdded = new CarServiceOwner(firstName, lastName, pesel, postalAddress);
        }
        else {
            userAdded = new Person(firstName, lastName, pesel, postalAddress);
        }
        dataStock.addPerson(userAdded);
        System.out.println(userAdded.getName() + " - user added with ID assigned: " + userAdded.getId());
    }
}
