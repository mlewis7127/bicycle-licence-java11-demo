package uk.gov.dvla.poc.events;

import uk.gov.dvla.poc.model.Event;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PenaltyPointsAdded extends Event {

    private static String getFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return now.format(formatter);
    }
    public PenaltyPointsAdded(int points) {
        super(PenaltyPointsAdded.class.getSimpleName(), "Penalty points added to licence for cycling to quickly", getFormattedDateTime(), points);
    }
}
