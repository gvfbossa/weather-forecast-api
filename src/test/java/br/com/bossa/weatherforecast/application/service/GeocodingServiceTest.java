package br.com.bossa.weatherforecast.application.service;

import br.com.bossa.weatherforecast.application.exception.GeocodingNotFoundException;
import br.com.bossa.weatherforecast.domain.model.Geocoding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeocodingServiceTest {

    private RestTemplate restTemplate;
    private GeocodingService geocodingService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        geocodingService = new GeocodingService(restTemplate);
    }

    @Test
    void testGetCoordinatesFromZipCode_Success() {
        String zipcode = "90210";
        Geocoding mockResult = new Geocoding();
        mockResult.setLatitude("34.0942489");
        mockResult.setLongitude("-118.4114324");
        mockResult.setDisplayName("Beverly Hills, Los Angeles, CA");

        List<Geocoding> responseList = List.of(mockResult);
        ResponseEntity<List<Geocoding>> responseEntity = new ResponseEntity<>(responseList, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(org.springframework.http.HttpMethod.GET),
                eq(null),
                ArgumentMatchers.<ParameterizedTypeReference<List<Geocoding>>>any()
        )).thenReturn(responseEntity);

        Geocoding result = geocodingService.getCoordinatesFromZipCode(zipcode);

        assertNotNull(result);
        assertEquals("34.0942489", result.getLatitude());
        assertEquals("-118.4114324", result.getLongitude());
        assertEquals("Beverly Hills, Los Angeles, CA", result.getDisplayName());
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(org.springframework.http.HttpMethod.GET),
                eq(null),
                ArgumentMatchers.<ParameterizedTypeReference<List<Geocoding>>>any()
        );
    }

    @Test
    void testGetCoordinatesFromZipCode_NoResults() {
        String zipcode = "00000";

        ResponseEntity<List<Geocoding>> responseEntity = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(org.springframework.http.HttpMethod.GET),
                eq(null),
                ArgumentMatchers.<ParameterizedTypeReference<List<Geocoding>>>any()
        )).thenReturn(responseEntity);

        assertThrows(GeocodingNotFoundException.class, () -> geocodingService.getCoordinatesFromZipCode(zipcode));
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(org.springframework.http.HttpMethod.GET),
                eq(null),
                ArgumentMatchers.<ParameterizedTypeReference<List<Geocoding>>>any()
        );
    }

    @Test
    void testGetCoordinatesFromZipCode_HttpFailure() {
        String zipcode = "90210";

        ResponseEntity<List<Geocoding>> responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(
                anyString(),
                eq(org.springframework.http.HttpMethod.GET),
                eq(null),
                ArgumentMatchers.<ParameterizedTypeReference<List<Geocoding>>>any()
        )).thenReturn(responseEntity);

        assertThrows(GeocodingNotFoundException.class, () -> geocodingService.getCoordinatesFromZipCode(zipcode));
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(org.springframework.http.HttpMethod.GET),
                eq(null),
                ArgumentMatchers.<ParameterizedTypeReference<List<Geocoding>>>any()
        );
    }
}
