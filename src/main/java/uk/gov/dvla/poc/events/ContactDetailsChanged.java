package uk.gov.dvla.poc.events;

import uk.gov.dvla.poc.model.Event;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContactDetailsChanged extends Event {

    private static String getFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return now.format(formatter);
    }
    public ContactDetailsChanged() {
        super(ContactDetailsChanged.class.getSimpleName(), "Customer has changed telephone number", getFormattedDateTime(), 0);
    }
}
