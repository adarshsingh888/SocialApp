package com.social.media.service;

import com.social.media.apiresponse.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class WeatherServices {

    @Value("${weather.api.key}")
    private String apiKey;

    private static final String API_URL = "http://api.weatherapi.com/v1/current.json?q=%s&key=%s";
    private static final String API="http://localhost:8080/social/public/adduser";
    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        String finalAPI = String.format(API_URL, city, apiKey);
        //User user= (User) User.builder().username("adarsh_singh").password("12345").build();
       // HttpEntity<User> httpEntity=new HttpEntity<>(user);
        ResponseEntity<WeatherResponse> res = restTemplate.exchange(finalAPI, HttpMethod.GET,/*httpEntity*/null, WeatherResponse.class);
        return res.getBody();
    }
}
