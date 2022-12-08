## Requirements

* Maven Compiler
* Java 17 version

[Api Swagger Doc](http://localhost:8080/swagger-ui/index.html#/)

### Task
```
Spring Boot, Maven
● Data link: https://raw.githubusercontent.com/mledoze/countries/master/countries.json
● The application exposes REST endpoint /routing/{origin}/{destination} that
returns a list of border crossings to get from origin to destination
● Single route is returned if the journey is possible
● Algorithm needs to be efficient
● If there is no land crossing, the endpoint returns HTTP 400
● Countries are identified by cca3 field in country data
● HTTP request sample (land route from Czech Republic to Italy):
○ GET /routing/CZE/ITA HTTP/1.0 :
{
"route": ["CZE", "AUT", "ITA"]
}

Expected deliveries:
1. Source code
2. Instructions on how to build and run the application
```

## Features
* When application start running, Our data provider fetch country data from [source](https://raw.githubusercontent.com/mledoze/countries/master/countries.json)
* Fetched data is added to our <b>cached</b> list
* 

 