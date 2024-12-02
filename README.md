# API Mortgage Calculator Service
A service that allows users to view bank mortgage loan interest rates and calculate the maximum eligible loan amount based on the customer's income.

## Build and Test
    - Use 'mvc clean install' to build the application and run unit and spring boot integration test.

## Run application
    - Use 'java -jar .\mortgage-calculator-service-1.0.0.jar --spring.profiles.active=test' from the jar file location.

## Test API using Swagger UI
    - Use swagger to test the API's - http://localhost:8080/swagger-ui/index.html.

## Requirement assumptions:
1) Interest rate hard coded and stored in the in memory cache. Which will be used throughout the application.
2) Added a 'message' field to the mortgage response to explain why the customer is not eligible for the loan.
3) The 'maturityPeriod' represents the duration of the mortgage in years. 