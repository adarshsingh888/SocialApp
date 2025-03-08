package com.socialmedia.social.media.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
    @JsonProperty("current")
    private Current current;

    @Getter
    @Setter
    public static class Current {
        @JsonProperty("last_updated")
        private String lastUpdated;

        @JsonProperty("temp_c")
        private double tempC;

        @JsonProperty("temp_f")
        private double tempF;

        @JsonProperty("condition")
        private Condition condition;
    }

    @Getter
    @Setter
    public static class Condition {
        @JsonProperty("text")
        private String text;

        @JsonProperty("icon")
        private String icon;

        @JsonProperty("code")
        private int code;
    }
}
