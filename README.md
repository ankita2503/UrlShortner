# URL Shortener Service
This project is a URL shortener service which takes a long URL and generates a short URL. It is built using Spring Boot and Redis.

## Table of Contents
Technologies Used
Setup
Usage
Endpoints
Contributing

## Technologies Used
Spring Boot

Redis

Apache Commons Validator

Lombok

## Setup
Clone the repository.
Make sure you have Redis installed and running on your system.
Build and run the application using mvn clean install and java -jar target/URLShortener-0.0.1-SNAPSHOT.jar.
Usage
The application provides two endpoints for URL shortening:

### POST 

/tinyurl: This endpoint takes a long URL and generates a short URL. It returns the generated URL ID as a response header.

### GET 

/tinyurl/{id}: This endpoint takes the URL ID and redirects the user to the original long URL.


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
