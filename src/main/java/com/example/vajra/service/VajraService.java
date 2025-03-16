package com.example.vajra.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class VajraService {

    private static final String API_URL = "https://graphhopper.com/api/1/route?point={start}&point={end}&vehicle=car&key={key}";

    public double getDistance(double startLat, double startLon, double endLat, double endLon, String apiKey) {
        RestTemplate restTemplate = new RestTemplate();
        String start = startLat + "," + startLon;
        String end = endLat + "," + endLon;
        String url = API_URL.replace("{start}", start)
                            .replace("{end}", end)
                            .replace("{key}", apiKey);

        // Make the API request
        String response = restTemplate.getForObject(url, String.class);

        // Parse the distance from the JSON response
        // Note: For production, use a proper JSON parser like Jackson
        if (response != null && response.contains("\"distance\":")) {
            int distanceIndex = response.indexOf("\"distance\":") + 11;
            int distanceEndIndex = response.indexOf(",", distanceIndex);
            String distanceStr = response.substring(distanceIndex, distanceEndIndex);
            return Double.parseDouble(distanceStr) / 1000; // Convert meters to kilometers
        }
        return -1; // Error case
    }
    
   private static final String API_KEY = "da01578b95044c198fba918cdd43d95f";
   private static final String BASE_URL = "https://api.opencagedata.com/geocode/v1/json";
    
   public String getAutocompleteSuggestions(String query) throws IOException{
	   String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
	   
	   String url = BASE_URL + "?q=" + encodedQuery + "&key=" + API_KEY;
	   
	   try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
           HttpGet request = new HttpGet(url);
           try (CloseableHttpResponse response = httpClient.execute(request)) {
               // Parse the JSON response
               String jsonResponse = EntityUtils.toString(response.getEntity());
               ObjectMapper objectMapper = new ObjectMapper();
               JsonNode rootNode = objectMapper.readTree(jsonResponse);

               // Return the results as a JSON string
               return rootNode.path("results").toString();
		   
	   }
	   
   } 
   }
   
   
   
   
   
   
}