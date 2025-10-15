package br.com.bossa.weatherforecast.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private String location;
    private double latitude;
    private double longitude;

    @JsonProperty("current_weather")
    private CurrentWeather currentTemperature;

    private DailyWeather daily;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentWeather {
        private Double temperature;
        private Double windspeed;
        private Integer is_day;
        private Integer weathercode;
        private Double winddirection;
        private String time;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyWeather {
        @JsonProperty("temperature_2m_max")
        private List<Double> temperatureMax;

        @JsonProperty("temperature_2m_min")
        private List<Double> temperatureMin;

        private List<String> time;
    }
}
