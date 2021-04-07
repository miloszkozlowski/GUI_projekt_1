package pl.mihome.s23419p01.menu;

import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.rent.Rentable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RentRentableMenu extends MenuClass {

    Map<Integer, Person> usersForRental = new HashMap<>();
    Rentable rentable;

    public RentRentableMenu(Rentable rentable) {
        super();
        this.rentable = rentable;
        this.title = "New rent of " + rentable.toString();
    }

    @Override
    void printMenuDetails() {
        int i = 1;
        for(Person person: dataStock.getPeople()) {
            if(person != dataStock.getCurrentUser()) {
                System.out.println(i + ". " + person.getName());
                usersForRental.put(i++, person);
            }
        }
        possibleChoices.addAll(usersForRental.keySet());
        possibleChoices.add(i);
        pickYourNumber(i);
    }

    @Override
    Menu handleChoice(int choice) {
        if(choice == possibleChoices.size()-1) {
            return new WarehouseDetailMenu(rentable);
        }
        else {
            Person userForRent = usersForRental.get(choice);
            System.out.print("Enter amount of rental days: ");
            if(scanner.hasNextInt()) {
                int days = scanner.nextInt();
                System.out.println("Cost of rent will be PLN " + rentable.getDailyRentFee().multiply(BigDecimal.valueOf(days)));
                try {
                    LocalDate today = dataStock.getCurrentDate();
                    rentable.rent(userForRent, today, today.plusDays(days));
                    System.out.println("This warehouse was rented to " + userForRent.getName() + " until " + rentable.getRentEndDate());
                    return new OwnerMenu();
                }
                catch (IllegalAccessException | IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    return new WarehouseDetailMenu(rentable);
                }
            }
            else {
                return new RentRentableMenu(rentable);
            }
        }
    }
}
