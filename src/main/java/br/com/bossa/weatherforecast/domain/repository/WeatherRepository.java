package br.com.bossa.weatherforecast.domain.repository;

import br.com.bossa.weatherforecast.web.dto.WeatherDTO;

public interface WeatherRepository {
    WeatherDTO findByZipcode(String zipcode);
    void save(String zipcode, WeatherDTO dto);
}
