package uk.gov.dvla.poc.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.dvla.poc.model.BicycleLicence;
import com.amazonaws.regions.Regions;

import java.util.*;

@Slf4j
@Repository
public class BicycleLicenceDynamoDBRepository implements CrudRepository<BicycleLicence, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_WEST_1)
            .build();

    private final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

    {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public <S extends BicycleLicence> S save(S s) {
        log.info("Inserting/Updating licence document in the DynamoDB table...");
        dynamoDBMapper.save(s);
        return s;
    }

    @Override
    public <S extends BicycleLicence> Iterable<S> saveAll(Iterable<S> iterable) {
        log.info("Inserting/Updating multiple licence documents in the DynamoDB table...");
        List<S> savedEntities = new ArrayList<>();
        for (S entity : iterable) {
            savedEntities.add(save(entity));
        }
        return savedEntities;
    }

    @Override
    public Optional<BicycleLicence> findById(String id) {
        log.info("Finding licence document by ID in the DynamoDB table...");
        return Optional.ofNullable(dynamoDBMapper.load(BicycleLicence.class, id));
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public Iterable<BicycleLicence> findAll() {
        log.info("Retrieving all licences from DynamoDB table...");
        return dynamoDBMapper.scan(BicycleLicence.class, new DynamoDBScanExpression());
    }

    public BicycleLicence findByEmail(String email) {
        log.info("Finding licence document by email in the DynamoDB table..." + email);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":email", new AttributeValue().withS(email));

        DynamoDBQueryExpression<BicycleLicence> queryExpression = new DynamoDBQueryExpression<BicycleLicence>()
            .withIndexName("email-index")
            .withConsistentRead(false)  // GSI doesn't support consistent reads
            .withKeyConditionExpression("email = :email")
            .withExpressionAttributeValues(eav);

        List<BicycleLicence> licences = dynamoDBMapper.query(BicycleLicence.class, queryExpression);

        log.info("Found {} licences for email {}", licences.size(), email);

        return licences.isEmpty() ? null : licences.get(0);
    }

    @Override
    public Iterable<BicycleLicence> findAllById(Iterable<String> ids) {
        log.info("Finding multiple licence documents by IDs in the DynamoDB table...");
        List<BicycleLicence> results = new ArrayList<>();
        for (String id : ids) {
            findById(id).ifPresent(results::add);
        }
        return results;
    }

    @Override
    public long count() {
        log.info("Counting all licences in the DynamoDB table...");
        return dynamoDBMapper.count(BicycleLicence.class, new DynamoDBScanExpression());
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting licence document by ID from the DynamoDB table...");
        BicycleLicence licence = dynamoDBMapper.load(BicycleLicence.class, id);
        if (licence != null) {
            dynamoDBMapper.delete(licence);
        }
    }

    @Override
    public void delete(BicycleLicence licence) {
        log.info("Deleting licence document from the DynamoDB table...");
        dynamoDBMapper.delete(licence);
    }

    @Override
    public void deleteAll(Iterable<? extends BicycleLicence> licences) {
        log.info("Deleting multiple licence documents from the DynamoDB table...");
        for (BicycleLicence licence : licences) {
            delete(licence);
        }
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all licence documents from the DynamoDB table...");
        List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBMapper.batchDelete(findAll());
        if (!failedBatches.isEmpty()) {
            log.error("Failed to delete {} batches", failedBatches.size());
            StringBuilder errorDetails = new StringBuilder();
            for (DynamoDBMapper.FailedBatch batch : failedBatches) {
                if (batch != null && batch.getException() != null) {
                    errorDetails.append(batch.getException().getMessage()).append("; ");
                }
            }
            String errorMsg = errorDetails.length() > 0 ? errorDetails.toString() : "Unknown error";
            throw new RuntimeException("Failed to delete " + failedBatches.size() + " batches: " + errorMsg);
        }
    }
}