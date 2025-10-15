package br.com.bossa.weatherforecast.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GeocodingNotFoundException extends RuntimeException {

    public GeocodingNotFoundException(String zipCode) {
        super("Coordinates not found for ZIP code: " + zipCode);
    }
}
