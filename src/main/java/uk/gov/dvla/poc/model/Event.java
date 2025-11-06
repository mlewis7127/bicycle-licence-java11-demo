package uk.gov.dvla.poc.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class Event {

    @DynamoDBAttribute
    private String eventName;

    @DynamoDBAttribute
    private String eventDescription;

    @DynamoDBAttribute
    private String transactionDTM;

    @DynamoDBAttribute
    private int penaltyPoints;

}
