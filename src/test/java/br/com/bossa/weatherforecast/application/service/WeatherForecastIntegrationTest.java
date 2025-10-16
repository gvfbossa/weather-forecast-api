package br.com.bossa.weatherforecast.application.service;

import br.com.bossa.weatherforecast.web.dto.WeatherDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherForecastIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private org.springframework.data.redis.connection.RedisConnectionFactory redisConnectionFactory;

    @Test
    void testGetWeatherIntegration() {
        ConcurrentMapCacheManager mockedCache = new ConcurrentMapCacheManager("weatherCache");
        when(cacheManager.getCache("weatherCache")).thenReturn(mockedCache.getCache("weatherCache"));

        String zipcode = "90210";

        var response = restTemplate.getForEntity("/api/weather/" + zipcode, WeatherDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("90210, Los Angeles, Los Angeles County, California, United States", response.getBody().getLocation());
        assertNotNull(response.getBody().getTemperature());

        assertTrue(Objects.requireNonNull(restTemplate.getForEntity("/api/weather/" + zipcode, WeatherDTO.class).getBody()).getFromCache());
    }
}