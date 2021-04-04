package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.person.Person;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IndependentCarServiceSpot extends CarServiceSpot implements Rentable {

    LocalDate rentFrom;
    LocalDate rentUntil;
    Person currentTenant;

    public IndependentCarServiceSpot(BigDecimal dailyServiceCost, CarService carService) {
        super(dailyServiceCost, carService);
    }

    @Override
    public IndependentCarServiceSpot rent(Person person, LocalDate from, LocalDate until) throws IllegalAccessException {
        if(currentTenant != null) {
            throw new IllegalAccessException("This spot is occupied.");
        }
        rentFrom = from;
        rentUntil = until;
        currentTenant = person;
        return this;
    }

    @Override
    public BigDecimal getDailyRentFee() {
        return dailyServiceCost;
    }

    @Override
    public boolean refreshCurrentState() {
        return false;
    }

    @Override
    public boolean isAvailableForRent() {
        return false;
    }

    @Override
    public LocalDate getRentEndDate() {
        return rentUntil;
    }

}
