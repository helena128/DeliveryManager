# Delivery Manager

### About project
A microservice that manages deliveries. As of now it supports the following functionality:
* Retrieving list of all deliveries not yet received or list of all deliveries that have been received already 
(behavior is determined by the input parameter)
* Marking delivery as received

On the appliation start, initial data will be loaded into the database. By default, it is taken from 
`${project-root}/data/deliveries.json` where project-root is the root of the delivery-manager project. This property can be overridden (see [Application Properties section](#application-properties))

### Built with
* [Kotlin 1.5](https://kotlinlang.org/)
* [OpenJDK 15](http://openjdk.java.net/projects/jdk/15/)
* [GraphQL-Kotlin](https://opensource.expediagroup.com/graphql-kotlin/docs/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Webflux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
* [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc)
* [H2 embedded database](https://www.h2database.com/html/main.html)
* [MapStruct Kotlin](https://github.com/mapstruct/mapstruct-examples/tree/master/mapstruct-kotlin)

### Getting started
To run the application, the following steps are required.

#### Prerequisites
* Docker Engine and docker-compose

#### Building application
To build and run application:
 1. clone project `git clone git@github.com:helena128/PaymentManager.git`
 2. navigate to the directory of the project `cd PaymentManager/`.
 3. run `docker-compose up --build`
 
 By default application starts on port `8080`.
 
#### Application properties
To pass properties either set env variables or pass following arguments to `java -jar` command:

| Application property | Description | Example |
| --- | --- | --- |
| `--delivery-manager.input-data-path` | Path to JSON file with init data | `../data/deliveries.json` |

If no initial data is required, the application can be run with the `test` profile (`-Dspring.profiles.active=test`).

### API
Documentation for the API is generated on the start of application and can be accessed via http://localhost:8080/sdl 
by default. 

For testing purposes, there is also http://localhost:8080/playground that can be used.

### Examples
##### Query to retrieve not yet received deliveries
```graphql
query {
	getDeliveries(received: false) {
        deliveryId
        deliveryStatus
        product
  }
} 
```
#### Query to retrieve received deliveries
```graphql
query {
	getDeliveries(received: true) {
        deliveryId
        deliveryStatus
        product
        updatedDate
  }
}
```
#### Mutation to mark delivery received
```graphql
mutation {
    markDeliveryReceived(id: "101") {
        deliveryStatus
        deliveryId
        product
    }
}
```
##### Exceptions
If updated entity has already been marked as received or it is non-existent in the system, 
the response will be similar to this one:
```graphql
{
  "errors": [
    {
      "message": "Exception while fetching data (/markDeliveryReceived) : Couldn't update entity with deliveryId 101",
      "locations": [
        {
          "line": 2,
          "column": 3
        }
      ],
      "path": [
        "markDeliveryReceived"
      ]
    }
  ]
}
```

#### Contact
In case of any questions feel free to contact [Elena Cheprasova](mailto:elenatchepr@gmail.com?subject=[GitHub]).