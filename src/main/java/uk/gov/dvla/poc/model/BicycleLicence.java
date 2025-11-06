package uk.gov.dvla.poc.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "BicycleLicence")
public class BicycleLicence {

    @JsonIgnore
    @DynamoDBAttribute
    private String documentId;

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String name;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "email-index")
    private String email;

    @DynamoDBAttribute
    private String telephone;

    @DynamoDBAttribute
    private int penaltyPoints;

    @DynamoDBAttribute
    private List<Event> events = new ArrayList<>();

    public void addEvent(Event evt) {
        events.add(evt);
    }

}
