package pl.mihome.s23419p01.model.rent;

import pl.mihome.s23419p01.model.CustomerProperty;

import java.util.Set;
import java.util.UUID;

public interface Insertable {
    void insertItem(CustomerProperty item);

    CustomerProperty removeItem(UUID itemId);

    Set<CustomerProperty> clear();
}
