# Easy Inventory

Restful CRUD API for a easy-inventory application using Spring Boot, Mysql, JPA and Hibernate.

## Requirements

    1. Java - 1.8.x

    2. Maven - 3.x.x

    3. Mysql - 5.x.x

## Steps to Setup

**1. Clone the application**

```bash
git@github.com:malikdul/easy-inventory.git
```

**2. Create Mysql database**
```bash
create database easyinventory
```

**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**2. Build and run the app using maven**

```bash
mvn package
java -jar target/easy-inventory-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

Swagger: http://localhost:8080/swagger-ui.html
WADL: http://localhost:8080/api/application.wadl


## QA

SoapUI project is in QA folder (few APIs are covered for now)
    


