package uk.gov.dvla.poc.repository;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.dvla.poc.model.BicycleLicence;
import uk.gov.dvla.poc.model.dynamo.Activity;
import uk.gov.dvla.poc.model.dynamo.LicenceActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class LicenceActivityDynamoRepository  implements CrudRepository<LicenceActivity, String> {

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_WEST_1)
            .build();

    private DynamoDB dynamoDB = new DynamoDB(client);

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TABLE_NAME = "licence-dev";

    @Override
    public <S extends LicenceActivity> S save(S s) {
        return null;
    }

    @Override
    public <S extends LicenceActivity> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<LicenceActivity> findById(String s) {
        Table table = dynamoDB.getTable(TABLE_NAME);
        log.info("Getting record by id: " + s);
        HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("id", new AttributeValue().withS(s));
        GetItemRequest getItemRequest = new GetItemRequest()
                .withTableName(TABLE_NAME)
                .withKey(key);
        GetItemResult activityItemResult = client.getItem(getItemRequest);
        Map<String, AttributeValue> activityItem = activityItemResult.getItem();
        log.info("Activity Item from Dynamo: " + activityItem);
        if(activityItem == null) {
            return null;
        } else {
            LicenceActivity activity = new LicenceActivity();
            activity.setId(activityItem.get("id").getS());
            activity.setPenaltyPoints(Integer.parseInt(activityItem.get("PenaltyPoints").getN()));

            // Parse event string to json
            try {
                String eventString = activityItem.get("Activity").getS();
                Activity[] events = objectMapper.readValue(eventString, Activity[].class);
                for (Activity event : events) {
                    Activity activityModel = new Activity();
                    activityModel.setEventDescription(event.getEventDescription());
                    activityModel.setEventName(event.getEventName());
                    activityModel.setEventDate(event.getEventDate());
                    activity.addActivity(activityModel);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return Optional.of(activity);
        }


    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<LicenceActivity> findAll() {
        return null;
    }

    @Override
    public Iterable<LicenceActivity> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(LicenceActivity bicycleLicenceActivity) {

    }

    @Override
    public void deleteAll(Iterable<? extends LicenceActivity> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
