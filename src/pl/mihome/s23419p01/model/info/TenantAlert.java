package pl.mihome.s23419p01.model.info;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TenantAlert implements Info {
    private final UUID personId;
    private final UUID warehouseId;
    private final LocalDate alertDate;
    private final BigDecimal alertAmount;

    public TenantAlert(UUID personId, UUID warehouseId, LocalDate alertDate, BigDecimal alertAmount) {
        this.personId = personId;
        this.warehouseId = warehouseId;
        this.alertDate = alertDate;
        this.alertAmount = alertAmount;
    }

    public UUID getPersonId() {
        return personId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    @Override
    public LocalDate getInfoDate() {
        return alertDate;
    }

    public BigDecimal getAlertAmount() {
        return alertAmount;
    }
}
