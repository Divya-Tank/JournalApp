package com.Divya.Service;

import com.Divya.Cache.AppCache;
import com.Divya.Response.WeatherResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class WeatherAPIservice {

    @Value("${weather.api.key}")
    private String APIkey;
    @Autowired
    private AppCache appCache;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String City){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + City, WeatherResponse.class);
        if(weatherResponse!=null){
            return weatherResponse;
        }else {
            String finalUrl = appCache.appCache.get(AppCache.Keys.WEATHER_API.toString()).replace("<APIKEY>",APIkey).replace("<CITY>",City);
            ResponseEntity<WeatherResponse> respone = restTemplate.exchange(finalUrl, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = respone.getBody();
            if(body!=null){
                redisService.set("weather_of_"+City,body,5L);
            }
            return body;
        }

    }

}
