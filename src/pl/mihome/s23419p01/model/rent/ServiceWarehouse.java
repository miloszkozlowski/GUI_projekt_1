package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.model.person.Person;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServiceWarehouse extends RentableArea {

    public ServiceWarehouse(BigDecimal areaCubicMeters, BigDecimal dailyPrice) {
        super(areaCubicMeters, dailyPrice);
    }

    public ServiceWarehouse(BigDecimal areaSquareMetersMeters, BigDecimal height, BigDecimal dailyPrice) {
        super(areaSquareMetersMeters, height, dailyPrice);
    }


    @Override
    public ServiceWarehouse rent(Person person, LocalDate from, LocalDate until) throws IllegalAccessException {
        throw new IllegalAccessException("Service warehouse is not for rent doh!");
    }

}
