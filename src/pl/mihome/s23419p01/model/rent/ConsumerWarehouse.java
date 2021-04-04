package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.exception.TooManyThingsException;
import pl.mihome.s23419p01.model.CustomerProperty;
import pl.mihome.s23419p01.model.person.Person;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConsumerWarehouse extends RentableArea implements Insertable {

    public ConsumerWarehouse(BigDecimal areaCubicMeters, BigDecimal dailyPrice) {
        super(areaCubicMeters, dailyPrice);
    }

    public ConsumerWarehouse(BigDecimal lengthMeters, BigDecimal widthMeters, BigDecimal heightMeters, BigDecimal dailyPrice) {
        super(lengthMeters, widthMeters, heightMeters, dailyPrice);
    }

    @Override
    public ConsumerWarehouse rent(Person person, LocalDate from, LocalDate until) throws IllegalAccessException {
        return (ConsumerWarehouse) super.rent(person, from, until);
    }

    @Override
    public void insertItem(CustomerProperty item) {
        BigDecimal occupiedSpace = properties.stream()
                .map(CustomerProperty::getSpaceCubicMeters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (occupiedSpace.add(item.getSpaceCubicMeters()).compareTo(areaCubicMeters) > 0) {
            properties.add(item);
        }
        else {
            throw new TooManyThingsException("Remove some old items to insert a new item");
        }
    }

    @Override
    public CustomerProperty removeItem(UUID itemId) {
        CustomerProperty item = properties.stream()
                .filter(property -> property.getId().equals(itemId))
                .findFirst()
                .orElseThrow();
        properties.remove(item);
        return item;
    }

    @Override
    public Set<CustomerProperty> clear() {
        Set<CustomerProperty> toReturn = new HashSet<>(properties);
        properties.clear();
        return toReturn;
    }

    @Override
    public String toString() {
        return "Consumer Warehouse, space: " + areaCubicMeters + "m3, price: PLN " + dailyPrice + " per day";
    }

}
