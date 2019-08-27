package com.city.weather.app.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class CityAppController {

    private static final Logger _logger = Logger.getLogger(CityAppController.class);

    @Autowired
    CityWeatherInfoService  cityWeatherInfoService;

    @RequestMapping(method = RequestMethod.GET, path="/hello")
    public String hello(){
        _logger.info("Hello City App Controller");
        return "Hello";
    }

    @RequestMapping(method = RequestMethod.POST, path="/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody String getWeatherInfo(@RequestParam String cities){
        _logger.info("Get Weather Info for " + cities);
        List<String> citiesList = Arrays.asList(cities.split(","));
        String cityWeatherInfoList = cityWeatherInfoService.getWeatherInfoAsJsonArrayString(citiesList);
        return cityWeatherInfoList;
    }
}
