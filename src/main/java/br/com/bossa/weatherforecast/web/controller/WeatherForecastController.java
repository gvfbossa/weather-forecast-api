package br.com.bossa.weatherforecast.web.controller;

import br.com.bossa.weatherforecast.application.service.WeatherForecastService;
import br.com.bossa.weatherforecast.web.dto.WeatherDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@Tag(name = "Weather Forecast API", description = "Endpoints to retrieve weather information")
public class WeatherForecastController {

    private final WeatherForecastService weatherForecastService;

    public WeatherForecastController(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    @Operation(
            summary = "Retrieve weather by zipcode",
            description = "Fetches the current weather, daily max/min, and upcoming forecast for a given zipcode.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Weather retrieved", content = @Content(schema = @Schema(implementation = WeatherDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)
            }
    )
    @GetMapping("/{zipcode}")
    public ResponseEntity<WeatherDTO> getWeatherInformation(@PathVariable String zipcode) {
        WeatherDTO weatherDTO = weatherForecastService.retrieveWeatherInformation(zipcode);
        return ResponseEntity.ok(weatherDTO);
    }
}
