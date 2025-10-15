package br.com.bossa.weatherforecast.application.service;

import br.com.bossa.weatherforecast.domain.model.Weather;
import br.com.bossa.weatherforecast.domain.repository.WeatherRepository;
import br.com.bossa.weatherforecast.web.dto.DailyForecastDTO;
import br.com.bossa.weatherforecast.web.dto.WeatherDTO;
import br.com.bossa.weatherforecast.web.mapper.WeatherMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherForecastServiceTest {

    private WeatherFetchService weatherFetchService;
    private WeatherMapper weatherMapper;
    private WeatherRepository weatherRepository;
    private WeatherForecastService service;

    @BeforeEach
    void setUp() {
        weatherFetchService = mock(WeatherFetchService.class);
        weatherMapper = mock(WeatherMapper.class);
        weatherRepository = mock(WeatherRepository.class);

        service = new WeatherForecastService(weatherFetchService, weatherMapper, weatherRepository);
    }

    @Test
    void testRetrieveWeatherInformation_FromService_NotCached() {
        String zipcode = "90210";
        Weather weather = new Weather();

        List<DailyForecastDTO> upcomingDays = List.of(
                new DailyForecastDTO("2025-09-26", 16.9, 22.2),
                new DailyForecastDTO("2025-09-27", 17.5, 25.5)
        );

        WeatherDTO dto = WeatherDTO.builder()
                .location("Beverly Hills")
                .temperature(25.0)
                .dailyMax(30.0)
                .dailyMin(20.0)
                .fromCache(false)
                .upcomingDays(upcomingDays)
                .build();

        when(weatherRepository.findByZipcode(zipcode)).thenReturn(null);
        when(weatherFetchService.fetchWeatherFromCoordinates(zipcode)).thenReturn(weather);
        when(weatherMapper.toWeatherDTO(weather)).thenReturn(dto);

        WeatherDTO result = service.retrieveWeatherInformation(zipcode);

        assertNotNull(result);
        assertFalse(result.getFromCache());
        assertEquals("Beverly Hills", result.getLocation());
        assertEquals(25.0, result.getTemperature());
        assertNotNull(result.getUpcomingDays());
        assertEquals(2, result.getUpcomingDays().size());

        verify(weatherRepository, times(1)).save(zipcode, dto);
        verify(weatherFetchService, times(1)).fetchWeatherFromCoordinates(zipcode);
    }

    @Test
    void testRetrieveWeatherInformation_FromCache() {
        String zipcode = "90210";

        List<DailyForecastDTO> upcomingDays = List.of(
                new DailyForecastDTO("2025-09-26", 16.9, 22.2),
                new DailyForecastDTO("2025-09-27", 17.5, 25.5)
        );

        WeatherDTO cachedDto = WeatherDTO.builder()
                .location("Beverly Hills")
                .temperature(25.0)
                .dailyMax(30.0)
                .dailyMin(20.0)
                .fromCache(true)
                .upcomingDays(upcomingDays)
                .build();

        when(weatherRepository.findByZipcode(zipcode)).thenReturn(cachedDto);

        WeatherDTO result = service.retrieveWeatherInformation(zipcode);

        assertNotNull(result);
        assertTrue(result.getFromCache());
        assertEquals("Beverly Hills", result.getLocation());
        assertEquals(25.0, result.getTemperature());
        assertNotNull(result.getUpcomingDays());
        assertEquals(2, result.getUpcomingDays().size());

        verify(weatherFetchService, never()).fetchWeatherFromCoordinates(any());
        verify(weatherRepository, never()).save(any(), any());
    }
}
