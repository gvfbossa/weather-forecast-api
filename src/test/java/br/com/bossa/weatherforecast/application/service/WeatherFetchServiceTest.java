package br.com.bossa.weatherforecast.application.service;

import br.com.bossa.weatherforecast.application.exception.WeatherNotFoundException;
import br.com.bossa.weatherforecast.domain.model.Geocoding;
import br.com.bossa.weatherforecast.domain.model.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherFetchServiceTest {

    private GeocodingService geocodingService;
    private RestTemplate restTemplate;
    private WeatherFetchService weatherFetchService;

    @BeforeEach
    void setUp() {
        geocodingService = mock(GeocodingService.class);
        restTemplate = mock(RestTemplate.class);
        weatherFetchService = new WeatherFetchService(restTemplate, geocodingService);
    }

    @Test
    void testFetchWeather_Success() {
        String zipcode = "90210";
        Geocoding geocoding = new Geocoding();
        geocoding.setLatitude("34.0942489");
        geocoding.setLongitude("-118.4114324");
        geocoding.setDisplayName("Beverly Hills, Los Angeles, CA");

        Weather weather = new Weather();

        when(geocodingService.getCoordinatesFromZipCode(zipcode)).thenReturn(geocoding);
        when(restTemplate.getForEntity(anyString(), eq(Weather.class)))
                .thenReturn(new ResponseEntity<>(weather, HttpStatus.OK));

        Weather result = weatherFetchService.fetchWeatherFromCoordinates(zipcode);

        assertNotNull(result);
        assertEquals("Beverly Hills, Los Angeles, CA", result.getLocation());
        verify(geocodingService, times(1)).getCoordinatesFromZipCode(zipcode);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Weather.class));
    }

    @Test
    void testFetchWeather_GeocodingNotFound() {
        String zipcode = "00000";

        when(geocodingService.getCoordinatesFromZipCode(zipcode)).thenReturn(null);

        assertThrows(WeatherNotFoundException.class,
                () -> weatherFetchService.fetchWeatherFromCoordinates(zipcode));

        verify(restTemplate, never()).getForEntity(anyString(), eq(Weather.class));
    }

    @Test
    void testFetchWeather_HttpFailure() {
        String zipcode = "90210";
        Geocoding geocoding = new Geocoding();
        geocoding.setLatitude("34.0942489");
        geocoding.setLongitude("-118.4114324");
        geocoding.setDisplayName("Beverly Hills, Los Angeles, CA");

        when(geocodingService.getCoordinatesFromZipCode(zipcode)).thenReturn(geocoding);
        when(restTemplate.getForEntity(anyString(), eq(Weather.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(WeatherNotFoundException.class,
                () -> weatherFetchService.fetchWeatherFromCoordinates(zipcode));

        verify(geocodingService, times(1)).getCoordinatesFromZipCode(zipcode);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Weather.class));
    }
}
