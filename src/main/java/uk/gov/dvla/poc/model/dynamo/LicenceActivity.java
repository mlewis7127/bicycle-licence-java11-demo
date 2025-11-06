package uk.gov.dvla.poc.model.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@DynamoDBTable(tableName="licence-dev")
public class LicenceActivity {

    @DynamoDBHashKey(attributeName="id")
    private String id;

    @DynamoDBAttribute(attributeName="PenaltyPoints")
    private int penaltyPoints;

    @DynamoDBAttribute(attributeName="Activity")
    @DynamoDBTypeConvertedJson
    private List<Activity> activity = new ArrayList<>();

    private int penaltyPointsAddedEvents;

    private int penaltyPointsRemovedEvents;

    private int nameChangedEvents;

    private int contactDetailsChangedEvents;

    @JsonCreator
    public LicenceActivity(@JsonProperty("id") final String id,
                           @JsonProperty("penaltyPoints") final int penaltyPoints) {
        this.id = id;
        this.penaltyPoints = penaltyPoints;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("penaltyPoints")
    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public void addActivity(Activity anActivity) {
        activity.add(anActivity);
    }

    @Override
    public String toString() {
        return "LicenceActivity{ id='" + id + '\'' +
                ", penaltyPoints='" + penaltyPoints + '\'' +
                '}';
    }

}
