package pl.mihome.s23419p01.model;

import pl.mihome.s23419p01.model.info.TenantAlert;
import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.service.DataStock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class BookEntry {
    private final UUID rentableId = UUID.randomUUID();
    private final UUID personId;
    private final UUID id;
    private final BigDecimal amountDue;
    private final LocalDate paymentDeadline;
    private boolean paid = false;
    private boolean notified = false;

    public BookEntry(UUID personId, UUID id, BigDecimal amountDue, LocalDate paymentDeadline) {
        this.personId = personId;
        this.id = id;
        this.amountDue = amountDue;
        this.paymentDeadline = paymentDeadline;
    }

    public UUID getRentableId() {
        return rentableId;
    }

    public UUID getPersonId() {
        return personId;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public LocalDate getPaymentDeadline() {
        return paymentDeadline;
    }

    public boolean isPaid() {
        return paid;
    }

    public boolean isNotified() {
        return notified;
    }

    public void settlePayment() {
        if(!paid) {
            paid = true;
        }
        else {
            throw new IllegalStateException("This is already paid man!");
        }
    }

    public void notifyDebtor() {
        if(!notified) {
            notified = true;
            DataStock dataStock = DataStock.getInstance();
            Person debtor = dataStock.getPeople().stream()
                    .filter(person -> person.getId().equals(personId))
                    .findFirst()
                    .orElseThrow();
            debtor.alert(new TenantAlert(personId, rentableId, dataStock.getCurrentDate(), amountDue));
        }
    }
}
