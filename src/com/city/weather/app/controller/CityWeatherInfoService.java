package com.city.weather.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CityWeatherInfoService {
    private static final Logger _logger = Logger.getLogger(CityWeatherInfoService.class);

    @Value("${api_key}")
    private  String API_KEY;

    @Value("${service_url}")
    private  String SERVICE_URL;

    @Value("${temperature_units}")
    private  String TEMP_UNITS;

    public String getWeatherInfoAsJsonArrayString(List<String> cities){
        _logger.debug("API_KEY == > " +  API_KEY);
        _logger.debug("SERVICE_URL == > " +  SERVICE_URL);
        _logger.debug("TEMP_UNITS == > " +  TEMP_UNITS);
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ArrayNode citiesWeatherInfo = nodeFactory.arrayNode();
        for(String city :cities){
            citiesWeatherInfo.add(getCityWeatherInfoJsonNode(city));
        }

        return citiesWeatherInfo.toString();
    }

    private JsonNode getCityWeatherInfoJsonNode(String city){
        _logger.info("Submitting weather map request ==> " + city);
        JsonNode jsonNode = null;
        try {

            //Testing purpose only to avoid having api key run out of max hits.

            /*InputStream is = this.getClass().getResourceAsStream("/sample.json");
            String str = IOUtils.toString(is, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(str);*/

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(SERVICE_URL)
                    .queryParam("q", city)
                    .queryParam("appid", API_KEY)
                    .queryParam("units",TEMP_UNITS);


            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            if(response.getStatusCode() == HttpStatus.OK){
                _logger.info("Received weather map response");
                ObjectMapper objectMapper = new ObjectMapper();
                _logger.debug("Response Payload ==> " + response.getBody());
                jsonNode = objectMapper.readTree(response.getBody());
            }


        }catch(Exception e){
            _logger.error("Error in Getting City Weather Info Json", e);
        }

        if(jsonNode == null){
            JsonNodeFactory nodeFactory = new JsonNodeFactory(){};
            jsonNode = nodeFactory.objectNode();
            ((ObjectNode) jsonNode).put("name",city);
            ((ObjectNode) jsonNode).put("notfound",true);
        }
        return jsonNode;
    }
}
