package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.exception.NeverRentException;
import pl.mihome.s23419p01.exception.ProblematicTenantException;
import pl.mihome.s23419p01.model.BookEntry;
import pl.mihome.s23419p01.model.CustomerProperty;
import pl.mihome.s23419p01.model.info.TenantAlert;
import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.service.DataStock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class RentableArea implements Rentable {

    public static final BigDecimal MAX_EXPENSES_PLN = BigDecimal.valueOf(1250);
    public static final int MAX_INFOS_AMOUNT_ACCEPTED = 3;

    List<Person> approvedToEnter = new ArrayList<>();
    Set<CustomerProperty> properties = new HashSet<>();
    LocalDate whenRentStarted;
    LocalDate whenRentEnds;
    BigDecimal dailyPrice;
    UUID id = UUID.randomUUID();
    BigDecimal areaCubicMeters;
    DataStock dataStock;

    public RentableArea(BigDecimal areaCubicMeters, BigDecimal dailyPrice) {
        if (areaCubicMeters.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Niewłaściwe dane pomieszczenia");
        }
        this.areaCubicMeters = areaCubicMeters;
        this.dailyPrice = dailyPrice;
        this.dataStock = DataStock.getInstance();
    }

    public Set<CustomerProperty> getProperties() {
        return properties;
    }

    public RentableArea(BigDecimal lengthMeters, BigDecimal widthMeters, BigDecimal heightMeters, BigDecimal dailyPrice) {
        this(lengthMeters.multiply(widthMeters).multiply(heightMeters), dailyPrice);
    }


    public RentableArea(BigDecimal areaSquareMeters, BigDecimal heightMeters, BigDecimal dailyPrice) {
        this(areaSquareMeters.multiply(heightMeters), dailyPrice);
    }

    public UUID getId() {
        return id;
    }

    public final Person whoIsPaying() {
        if (this instanceof ServiceWarehouse || approvedToEnter.isEmpty()) {
            return null;
        }
        return approvedToEnter.get(0);
    }

    @Override
    public RentableArea rent(Person person, LocalDate from, LocalDate until) throws IllegalAccessException {
        if (person.getAlerts().size() > MAX_INFOS_AMOUNT_ACCEPTED) {
            String rentables = person.getPayments().stream()
                    .map(BookEntry::getId)
                    .map(java.util.UUID::toString)
                    .reduce("", (concat, uuid) -> (concat + ", " + uuid));
            BigDecimal debt = person.getAlerts()
                    .stream()
                    .filter(alert -> alert instanceof TenantAlert)
                    .map(alert -> (TenantAlert)alert)
                    .map(TenantAlert::getAlertAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            throw new ProblematicTenantException("Osoba " + person.getName() + " posiadała już najem pomieszczen: " + rentables + " - PLN " + debt);
        }
        if (!approvedToEnter.isEmpty()) {
            throw new IllegalArgumentException("C'mon! This is already rented...");
        }
        if (from.isAfter(until) || from.isBefore(LocalDate.now()) || until.equals(from)) {
            throw new IllegalArgumentException("Oh shit! Provided dates are wrong.");
        }
        long duration = from.until(until, ChronoUnit.DAYS);
        BigDecimal fee = dailyPrice.multiply(BigDecimal.valueOf(duration));
        if (person.getUnpaidExpensesTotal().add(fee).compareTo(MAX_EXPENSES_PLN) > 0) {
            throw new IllegalArgumentException("Hey yo! Chill out with your expenses! You not gonna get this one...");
        }
        approvedToEnter.add(person);
        whenRentEnds = until;
        whenRentStarted = from;
        try {
            person.getFirstRentalDate();
        }
        catch (NeverRentException ex) {
            person.setFirstRentalDate(from);
        }
        dataStock.addPaymentEntry(new BookEntry(person.getId(), id, fee, until));
        return this;
    }

    @Override
    public BigDecimal getDailyRentFee() {
        return dailyPrice;
    }

    public final void manageApprovedToEnter(Person person, boolean adding) {
        if (approvedToEnter.isEmpty()) {
            throw new IllegalArgumentException("This is not rented - maybe you want it dude?");
        } else if (person.equals(whoIsPaying())) {
            throw new IllegalArgumentException("This guy is a tenant so he always has an access here.");
        } else if (adding) {
            approvedToEnter.add(person);
        } else {
            approvedToEnter.remove(person);
        }
    }

    public List<Person> getApprovedToEnter() {
        return approvedToEnter;
    }

    @Override
    public boolean isAvailableForRent() {
        return whenRentEnds == null;
    }

    @Override
    public boolean refreshCurrentState() {
        if(whenRentEnds == null) {
            return false;
        }
        LocalDate today = dataStock.getCurrentDate();
        if(today.isAfter(this.whenRentEnds)) {
            approvedToEnter.clear();
            whenRentEnds = null;
            whenRentStarted = null;
            return true;
        }
        return false;
    }

    public BigDecimal getAreaCubicMeters() {
        return areaCubicMeters;
    }

    @Override
    public LocalDate getRentEndDate() {
        return whenRentEnds;
    }
}
