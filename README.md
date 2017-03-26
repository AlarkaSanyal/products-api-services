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