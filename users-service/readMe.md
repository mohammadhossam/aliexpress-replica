# AliExpress Replica - Users Service

This project is part of the AliExpress Replica system, which consists of multiple microservices that together form an
e-commerce platform similar to AliExpress. The Users Service is responsible for managing buyer and merchant profiles.

## Table of Contents

- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Configuration](#configuration)
- [API Reference](#api-reference)
    - [Buyer](#buyer)
        - [Update Buyer](#update-buyer)
    - [Merchant](#merchant)
        - [Update Merchant](#update-merchant)


## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL
- Redis

## Getting Started

### Prerequisites

Make sure you have the following prerequisites installed on your machine:

- Java Development Kit (JDK) 8 or above
- Apache Maven
- PostgreSQL database
- Redis server

### Installation

1. Clone the AliExpress Replica repository:

   ```shell
   git clone https://github.com/mohammadhossam/aliexpress-replica/tree/master
   ```

2. Change to the project directory:
    ```shell  
    cd aliexpress-replica
   ```

3. Build the project using Maven:
    ```shell
   mvn clean install
    ```

### Configuration
1. Configure the PostgreSQL database by updating the `application.properties`
   file located in the `users-service/src/main/resources`
   directory. Set the appropriate values for the following properties:
   ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/your-database
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    ```
2. Configure the Redis server by updating the `application.properties` file. 
   Set the appropriate values for the following properties:
    ```properties
    spring.redis.host=localhost
    spring.redis.port=6379
    spring.redis.password=your-password
    ```
   
## API Reference

## Buyer 

### Update Buyer

Updates the buyer profile.

- **URL**
  `/api/buyer/update/{buyerId}`

- **Method**
  `PUT`

- **URL Parameters**
    - `buyerId` - The ID of the buyer to update.

- **Request Headers**
    - `Authorization` - The authentication token.
  
- **Request Body**
  The updated buyer object in JSON format:
  ```json
    {
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "new-password",
    "phoneNumber": "1234567890",
    "address": "123 Main St",
    "birthdate": "1990-01-01"
    }
    ```

- **Success Response**
    - **Status Code:** 200 (OK)
    - **Response Body:** "Buyer updated successfully"

- **Error Responses**
    - **Status Code:** 400 (Bad Request)
        - **Response Body:** Error message

    - **Status Code:** 403 (Forbidden)
        - **Response Body:** "User is not authenticated!"

    - **Status Code:** 406 (Non-Authoritative Information)
        - **Response Body:** "No authorization header exists!"


## Merchant 

### Update Merchant

Updates the merchant profile.

- **URL**
  `/api/merchant/update/{merchantId}`

- **Method**
  `PUT`

- **URL Parameters**
    - `merchantId` - The ID of the merchant to update.

- **Request Headers**
    - `Authorization` - The authentication token.
- **Request Body**
    ```json
    {
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@example.com",
    "password": "new-password",
    "phoneNumber": "9876543210",
    "address": "456 Market St",
    "birthdate": "1995-01-01",
    "taxNumber": "123456789"
    }
  ```

- **Success Response**
    - **Status Code:** 200 (OK)
    - **Response Body:** "Merchant updated successfully"

- **Error Responses**
    - **Status Code:** 400 (Bad Request)
        - **Response Body:** Error message

    - **Status Code:** 403 (Forbidden)
        - **Response Body:** "User is not authenticated!"

    - **Status Code:** 406 (Non-Authoritative Information)
        - **Response Body:** "No authorization header exists!"

   
