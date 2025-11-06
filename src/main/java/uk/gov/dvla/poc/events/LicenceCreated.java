package uk.gov.dvla.poc.events;

import uk.gov.dvla.poc.model.Event;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LicenceCreated extends Event {

    private static String getFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return now.format(formatter);
    }
    public LicenceCreated() {
        super(LicenceCreated.class.getSimpleName(), "Customer has created a Bicycle Licence", getFormattedDateTime(), 0);
    }
}