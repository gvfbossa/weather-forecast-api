package br.com.bossa.weatherforecast.web.controller;

import br.com.bossa.weatherforecast.application.service.WeatherForecastService;
import br.com.bossa.weatherforecast.web.dto.DailyForecastDTO;
import br.com.bossa.weatherforecast.web.dto.WeatherDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherForecastControllerTest {

    private WeatherForecastService weatherForecastService;
    private WeatherForecastController controller;

    @BeforeEach
    void setUp() {
        weatherForecastService = mock(WeatherForecastService.class);
        controller = new WeatherForecastController(weatherForecastService);
    }

    @Test
    void testGetWeatherInformation_ReturnsWeatherDTO() {
        String zipcode = "90210";

        List<DailyForecastDTO> upcomingDays = List.of(
                new DailyForecastDTO("2025-09-26", 16.9, 22.2),
                new DailyForecastDTO("2025-09-27", 17.5, 25.5)
        );

        WeatherDTO mockDto = WeatherDTO.builder()
                .location("Beverly Hills")
                .temperature(25.0)
                .dailyMax(30.0)
                .dailyMin(20.0)
                .fromCache(false)
                .upcomingDays(upcomingDays)
                .build();

        when(weatherForecastService.retrieveWeatherInformation(zipcode)).thenReturn(mockDto);

        ResponseEntity<WeatherDTO> response = controller.getWeatherInformation(zipcode);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        WeatherDTO body = response.getBody();
        assertNotNull(body);
        assertEquals("Beverly Hills", body.getLocation());

        assertEquals(25.0, body.getTemperature());
        assertNotNull(body.getUpcomingDays());
        assertEquals(2, body.getUpcomingDays().size());
        assertEquals("2025-09-26", body.getUpcomingDays().getFirst().getDate());

        verify(weatherForecastService, times(1)).retrieveWeatherInformation(zipcode);
    }
}
