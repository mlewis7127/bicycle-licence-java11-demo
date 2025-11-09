# Bicycle Licence DynamoDB Demo

This repository hosts a Java 11 codebase using Spring Boot for interacting with Amazon DynamoDB in a fictitious scenario of managing a Bicycle Licence application. The user interface allows you to:

- Register a new Bicycle Licence
- Update details such as contact number and penalty points
- View all the activity against a licence via Events

## Project Overview

This prototype was originally developed to highlight some capabilities within Amazon QLDB that were presented during an AWS online tech talk in March 2020. It was never intended to be production code and has no associated tests. The repository was made public to demonstrate the capabilities of the Amazon Q Developer Agent for Code Transformation.

Since Amazon Quantum Ledger Database (QLDB) was deprecated in July 2024, the codebase has been updated to replace QLDB with DynamoDB.

## Technologies and Frameworks

- Java 11
- Spring Boot (version 2.1.8.RELEASE)
- Maven (for dependency management and build)
- Amazon Web Services (AWS) SDK (version 1.11.693)
    - AWS Java SDK for DynamoDB
    - AWS Java SDK for STS
- Spring Boot Starter Dependencies:
    - spring-boot-starter-web
    - spring-boot-starter-thymeleaf
    - spring-boot-starter-data-jpa
    - spring-boot-starter-test
- Lombok (version 1.18.30)
- Testing:
    - JUnit Jupiter (version 5.5.2)
    - Mockito (version 3.12.4)

## Pre-Requisites

To run this prototype, you need to set up an Amazon DynamoDB table with the following specifications:

1. Table name: `BicycleLicence` with a partition key of `id` which is a String
2. Region: `eu-west-1`
3. Global Secondary Index: `email-index` with a partition key of `email`

Please note:
- The table name is hardcoded in an annotation in the `BicycleLicence` file.
- The region is hardcoded in the `BicycleLicenceDynamoDBRepository` file.

There is a CloudFormation template provided as part of this reposository to set this up. You can run it as follows:

```
aws cloudformation create-stack \
  --stack-name bicycle-licence-stack \
  --template-body file://bicycle-licence-table.yaml \
  --region eu-west-1
```

Check on the status of the deployment by running:

```
aws cloudformation describe-stacks \
  --stack-name bicycle-licence-stack \
  --region eu-west-1
```

You can delete the stack by running:

```
aws cloudformation delete-stack \
  --stack-name bicycle-licence-stack \
  --region eu-west-1
```


## Building and Running the Application

To build and run the application, use the following Maven command:

```
mvn spring-boot:run
```

This will start the Spring Boot application on the default port (usually 8080).

If using temporary credentials from the AWS SSO service, then run the following script:

```
./run.sh
```

This exports the temporary AWS credentials for a particular profile. Note this is hardcoded for a profile called `sandbox` so you will need to update this to your profile in the script.

## Testing

The project uses JUnit Jupiter for unit testing. To run the tests, use:

```
mvn test
```

## Note

This repository is intended for demonstration purposes only and should not be used as-is in a production environment.