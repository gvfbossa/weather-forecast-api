package br.com.bossa.weatherforecast.application.service;

import br.com.bossa.weatherforecast.application.exception.GeocodingNotFoundException;
import br.com.bossa.weatherforecast.domain.model.Geocoding;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class GeocodingService {

    private static final String GEOCODE_URL = "https://nominatim.openstreetmap.org";
    private final RestTemplate restTemplate;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Geocoding getCoordinatesFromZipCode(String zipCode) {
        String country = inferCountry(zipCode);
        String searchParam = country.equals("US") ? "postalcode" : "q";

        String uri = UriComponentsBuilder.fromUriString(GEOCODE_URL)
                .path("/search")
                .queryParam(searchParam, zipCode)
                .queryParam("country", country.equals("Brazil") ? "" : country)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();

        ResponseEntity<List<Geocoding>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Geocoding>>() {}
        );

        List<Geocoding> results = response.getBody();
        if (response.getStatusCode().is2xxSuccessful() && results != null && !results.isEmpty()) {
            return results.getFirst();
        } else {
            throw new GeocodingNotFoundException(zipCode);
        }
    }

    private String inferCountry(String zipcode) {
        if (zipcode.matches("\\d{5}-\\d{3}")) {
            return "Brazil";
        } else if (zipcode.matches("\\d{5}(-\\d{4})?")) {
            return "US";
        }
        return "";
    }

}