package br.com.bossa.weatherforecast.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WeatherNotFoundException extends RuntimeException {

    public WeatherNotFoundException(String zipcode) {
        super("Weather information not found for ZIP code: " + zipcode);
    }
}
