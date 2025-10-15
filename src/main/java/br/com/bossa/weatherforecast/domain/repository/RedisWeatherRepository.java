package br.com.bossa.weatherforecast.domain.repository;

import br.com.bossa.weatherforecast.web.dto.WeatherDTO;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

@Repository
public class RedisWeatherRepository implements WeatherRepository {
    private final CacheManager cacheManager;

    public RedisWeatherRepository(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public WeatherDTO findByZipcode(String zipcode) {
        Cache cache = cacheManager.getCache("weatherCache");
        return cache != null ? (WeatherDTO) cache.get(zipcode, WeatherDTO.class) : null;
    }

    @Override
    public void save(String zipcode, WeatherDTO dto) {
        Cache cache = cacheManager.getCache("weatherCache");
        if (cache != null) cache.put(zipcode, dto);
    }
}
