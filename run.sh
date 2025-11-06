#!/bin/bash
set -e

# Export AWS credentials for sandbox profile
# amazonq-ignore-next-line
eval $(aws configure export-credentials --profile sandbox --format env)

# Start the Spring Boot application
mvn spring-boot:run