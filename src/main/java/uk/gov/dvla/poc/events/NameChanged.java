package uk.gov.dvla.poc.events;

import uk.gov.dvla.poc.model.Event;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NameChanged extends Event {

    private static String getFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return now.format(formatter);
    }
    public NameChanged() {
        super(NameChanged.class.getSimpleName(), "Customer has changed name on licence", getFormattedDateTime(), 0);
    }
}
