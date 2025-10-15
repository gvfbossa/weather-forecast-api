package br.com.bossa.weatherforecast.application.service;

import br.com.bossa.weatherforecast.domain.model.Weather;
import br.com.bossa.weatherforecast.domain.repository.WeatherRepository;
import br.com.bossa.weatherforecast.web.dto.WeatherDTO;
import br.com.bossa.weatherforecast.web.mapper.WeatherMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WeatherForecastService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherForecastService.class);

    private final WeatherFetchService weatherFetchService;
    private final WeatherMapper weatherMapper;
    private final WeatherRepository weatherRepository;

    public WeatherForecastService(WeatherFetchService weatherFetchService,
                                  WeatherMapper weatherMapper,
                                  WeatherRepository weatherRepository) {
        this.weatherFetchService = weatherFetchService;
        this.weatherMapper = weatherMapper;
        this.weatherRepository = weatherRepository;
    }

    public WeatherDTO retrieveWeatherInformation(String zipcode) {
        logger.info("Retrieving weather for zipcode: {}", zipcode);

        WeatherDTO cachedDto = weatherRepository.findByZipcode(zipcode);
        if (cachedDto != null) {
            cachedDto.setFromCache(true);
            return cachedDto;
        }

        Weather weather = weatherFetchService.fetchWeatherFromCoordinates(zipcode);
        WeatherDTO dto = weatherMapper.toWeatherDTO(weather);
        dto.setFromCache(false);

        weatherRepository.save(zipcode, dto);

        return dto;
    }
}
