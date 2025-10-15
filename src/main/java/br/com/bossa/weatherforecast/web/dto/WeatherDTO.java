package br.com.bossa.weatherforecast.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String location;
    private Double temperature;
    private Double dailyMax;
    private Double dailyMin;
    private Boolean fromCache;
    private List<DailyForecastDTO> upcomingDays;
}
