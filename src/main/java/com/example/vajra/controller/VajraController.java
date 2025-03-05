package com.example.vajra.controller;

import com.example.vajra.service.VajraService;
import com.example.vajra.utils.Getcords;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VajraController {

    @Autowired
    private VajraService graphHopperService;

   
    private static final String API_KEY = "50b90f61-b4c5-4544-bf9a-5bcc36decbbf";

    @GetMapping("/distance")
    public String getDistance(@RequestParam String city1, @RequestParam String city2) {
        Getcords obj = new Getcords();
        city1 = city1.split(",")[0].trim();
        city2 = city2.split(",")[0].trim();
        try {
        	
            JSONObject city1Coords = obj.getCityCoordinates(city1);
            JSONObject city2Coords = obj.getCityCoordinates(city2);
            
            // Extract coordinates from the JSON
            String city1lat = city1Coords != null ? city1Coords.getString("latitude") : "N/A";
            String city1lon = city1Coords != null ? city1Coords.getString("longitude") : "N/A";
            
            String city2lat = city2Coords != null ? city2Coords.getString("latitude") : "N/A";
            String city2lon = city2Coords != null ? city2Coords.getString("longitude") : "N/A";
            
            // Parse the latitude and longitude to double
            double startLat = Double.parseDouble(city1lat);
            double startLon = Double.parseDouble(city1lon);
            double endLat = Double.parseDouble(city2lat);
            double endLon = Double.parseDouble(city2lon);
            
            // Use your API key to calculate the distance
            double distance = graphHopperService.getDistance(startLat, startLon, endLat, endLon, API_KEY);
            
            String Googlemapsurl =  "https://www.google.com/maps/dir/ " + city1 +  "/" +city2+ "/data=!3e0";
            
            if (distance > 0) {
            	return "Distance from your location: " + distance + " km <br>" +
            	        "<a href='" + Googlemapsurl + "' target='_blank'>Get Direction via Maps</a>";
            } else {
                return "Failed to calculate distance.";
            }

        }


        catch (Exception e) {
        	e.printStackTrace();
        	return "Error fetching city coordinates";
        }

       

 

    }
}
