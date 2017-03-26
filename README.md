# Products API
## Description
This project responds to the following http GET and PUT requests to deliver product data as JSON or update a product’s price in the data store.
### GET
Delivers product data as JSON.

    ** URL: /api/products/{id}
    ** URL Params: id=[number]
    
    ** Example:
     * URL: /api/products/13860428
     * Response: {"id":13860428,"name":"The Big Lebowski (Blu-ray)","current_price":{"value":16.65,"currencycode":"USD"}}
     
### PUT
Updates a product’s price in the database.

    ** URL: /api/products/{id}
    ** URL Params: id=[number]
    ** Request body: JSON, similar to the GET response
    
    ** Example:
     * URL: /api/products/13860428
     * Request body: {"id":13860428,"name":"The Big Lebowski (Blu-ray)","current_price":{"value":29.50,"currencycode":"USD"}}
     * Response: {"message":"Price has been updated"}

### Response codes
* **200 OK** : Product is found/updated
* **404 Not Found**: Product is not found in database
* **304 Not modified**: Product price is not updated
* **400 Bad request**: URL/request body has incorrect format

## Pre requisites
* Maven is installed
* Cassandra is installed

## Notes
Tomcat server port is set to 8014. This can be changed in *resources/config/application.properties* file.

## Running the application
Once application is running, the following options can be used to request the endpoint
* Advanced REST Client
* Postman

### Starting the application
#### Option 1 (Run JAVA exe jar file)
1. Download the jar file from https://github.com/AlarkaSanyal/products-api-services/blob/master/target/products-api-services-1.0-SNAPSHOT.jar
2. Open a terminal window and run the following command
```
\products-api-services>java -jar products-api-services-1.0-SNAPSHOT.jar
```
#### Option 2 (From terminal window)
1. Clone the code to your local repository from https://github.com/AlarkaSanyal/products-api-services
2. Open a terminal window and cd to the application parent folder
3. Run the following command
```
...\products-api-services>mvn spring-boot:run
```
#### Option 3 (From Eclipse/STS)
1. Clone the code to your local repository from https://github.com/AlarkaSanyal/products-api-services
2. Open Eclipse/STS
3. Import the project
4. Right-click on the project and "Run" as a "Spring Boot App"