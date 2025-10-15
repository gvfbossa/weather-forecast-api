package br.com.bossa.weatherforecast.application.service;

import br.com.bossa.weatherforecast.application.exception.WeatherNotFoundException;
import br.com.bossa.weatherforecast.domain.model.Geocoding;
import br.com.bossa.weatherforecast.domain.model.Weather;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherFetchService {

    private static final String FETCH_WEATHER_URL = "https://api.open-meteo.com";
    private final GeocodingService geocodingService;
    private final RestTemplate restTemplate;

    public WeatherFetchService(RestTemplate restTemplate, GeocodingService geocodingService) {
        this.restTemplate = restTemplate;
        this.geocodingService = geocodingService;
    }

    public Weather fetchWeatherFromCoordinates(String zipcode) {
        Geocoding geocoding = geocodingService.getCoordinatesFromZipCode(zipcode);
        if (geocoding == null) {
            throw new WeatherNotFoundException("Coordinates not found for zipcode: " + zipcode);
        }

        String uri = UriComponentsBuilder.fromUriString(FETCH_WEATHER_URL)
                .path("/v1/forecast")
                .queryParam("latitude", geocoding.getLatitude())
                .queryParam("longitude", geocoding.getLongitude())
                .queryParam("current_weather", true)
                .queryParam("temperature_unit", "celsius")
                .queryParam("daily", "temperature_2m_max,temperature_2m_min")
                .toUriString();

        ResponseEntity<Weather> response = restTemplate.getForEntity(uri, Weather.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new WeatherNotFoundException("Weather data not found for zipcode: " + zipcode);
        }

        Weather weather = response.getBody();
        weather.setLocation(geocoding.getDisplayName());
        return weather;
    }

}