package br.com.bossa.weatherforecast.web.mapper;

import br.com.bossa.weatherforecast.domain.model.Weather;
import br.com.bossa.weatherforecast.web.dto.DailyForecastDTO;
import br.com.bossa.weatherforecast.web.dto.WeatherDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(source = "location", target = "location")
    @Mapping(source = "currentTemperature.temperature", target = "temperature")
    @Mapping(source = "daily", target = "dailyMax", qualifiedByName = "firstMax")
    @Mapping(source = "daily", target = "dailyMin", qualifiedByName = "firstMin")
    @Mapping(source = "daily", target = "upcomingDays", qualifiedByName = "mapUpcomingDays")
    WeatherDTO toWeatherDTO(Weather weather);

    @Named("firstMax")
    default Double mapFirstMax(Weather.DailyWeather daily) {
        if (daily != null && daily.getTemperatureMax() != null && !daily.getTemperatureMax().isEmpty()) {
            return daily.getTemperatureMax().getFirst();
        }
        return null;
    }

    @Named("firstMin")
    default Double mapFirstMin(Weather.DailyWeather daily) {
        if (daily != null && daily.getTemperatureMin() != null && !daily.getTemperatureMin().isEmpty()) {
            return daily.getTemperatureMin().getFirst();
        }
        return null;
    }

    @Named("mapUpcomingDays")
    default List<DailyForecastDTO> mapUpcomingDays(Weather.DailyWeather daily) {
        if (daily == null || daily.getTime() == null) return List.of();
        List<DailyForecastDTO> upcoming = new ArrayList<>();
        for (int i = 1; i < daily.getTime().size(); i++) {
            upcoming.add(new DailyForecastDTO(
                    daily.getTime().get(i),
                    daily.getTemperatureMin().get(i),
                    daily.getTemperatureMax().get(i)
            ));
        }
        return upcoming;
    }
}
