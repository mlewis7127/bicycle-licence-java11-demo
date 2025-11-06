package uk.gov.dvla.poc.events;

import uk.gov.dvla.poc.model.Event;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PenaltyPointsRemoved extends Event {

    private static String getFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return now.format(formatter);
    }

    public PenaltyPointsRemoved(int points) {
        super(PenaltyPointsRemoved.class.getSimpleName(), "Penalty points removed from licence", getFormattedDateTime(), points);
    }
}
