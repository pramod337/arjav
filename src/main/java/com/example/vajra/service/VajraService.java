package com.example.vajra.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



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
    
   
    
    
}