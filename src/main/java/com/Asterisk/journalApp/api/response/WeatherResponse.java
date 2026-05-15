package com.Asterisk.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private Main main;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {

        @JsonProperty("feels_like")
        private double feelsLike;

        public double getFeelsLike() { return feelsLike; }
        public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
    }

    public Main getMain() { return main; }
    public void setMain(Main main) { this.main = main; }
}