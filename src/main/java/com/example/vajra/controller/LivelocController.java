package com.example.vajra.controller;

import com.example.vajra.service.VajraService;
import com.example.vajra.utils.Getcords;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LivelocController {

    @Autowired
    private VajraService graphHopperService;

    private static final String API_KEY = "50b90f61-b4c5-4544-bf9a-5bcc36decbbf";

    @GetMapping("/liveloc")
    public String saveLiveLocation(@RequestParam double lat, @RequestParam double lon, String city2) {

    	Getcords obj2 = new Getcords();
    	JSONObject city2Coords = obj2.getCityCoordinates(city2);
    	
    	String city2lat = city2Coords != null ? city2Coords.getString("latitude") : "N/A";
        String city2lon = city2Coords != null ? city2Coords.getString("longitude") : "N/A";
        
        
        double endLat = Double.parseDouble(city2lat);
        double endLon = Double.parseDouble(city2lon);
        
        //double startLat = 12.952282;
        //double startLon = 77.6053196;

        double startLat = lat;
        double startLon = lon;
       // double endLat = lat;
       // double endLon = lon;


        double distance = graphHopperService.getDistance(startLat, startLon, endLat, endLon, API_KEY);
        
        String graphHopperUrl = "https://graphhopper.com/maps/?point=" 
                + lat + "," + lon + "&point=" + endLat + "," + endLon + "&profile=car";
        
        System.out.println(city2);   
        
       String Googlemapsurl =  "https://www.google.com/maps/dir/ " + lat + "," + lon + "/" +city2+  "/@" +endLat+ "," +endLon+ "/data=!3e0";                   
        
       // System.out.println("Received Live Location: Latitude = " + lat + ", Longitude = " + lon);
        return "Distance from your location: " + distance + " km <br>" +
        "<a href='" + Googlemapsurl + "' target='_blank'>Get Direction via Maps</a>";
  }
}
