package uk.gov.dvla.poc.model.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

@Data
@DynamoDBDocument
public class Activity {

    @DynamoDBAttribute(attributeName = "eventName")
    private String eventName;
    @DynamoDBAttribute(attributeName = "eventDescription")
    private String eventDescription;
    @DynamoDBAttribute(attributeName = "eventDate")
    private String eventDate;

}
