package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.model.person.Person;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Rentable {

    Rentable rent(Person person, LocalDate from, LocalDate until) throws IllegalAccessException;

    BigDecimal getDailyRentFee();

    boolean refreshCurrentState();

    boolean isAvailableForRent();

    LocalDate getRentEndDate();
}
