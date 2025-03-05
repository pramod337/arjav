package com.example.vajra.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class Getcords {

    // Method to fetch coordinates for a city
    public static JSONObject getCityCoordinates(String city) {
    	String encodedAddress = null;
		try {
			encodedAddress = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String apiUrl = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json";

        try {
            String jsonResponse = sendGetRequest(apiUrl);
            JSONArray jsonArray = new JSONArray(jsonResponse);

            if (jsonArray.length() > 0) {
                JSONObject firstResult = jsonArray.getJSONObject(0);
                String lat = firstResult.getString("lat");
                String lon = firstResult.getString("lon");

                JSONObject cityCoords = new JSONObject();
                cityCoords.put("city", city);
                cityCoords.put("latitude", lat);
                cityCoords.put("longitude", lon);

                return cityCoords;
            } else {
                System.out.println("No results found for the city: " + city);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String sendGetRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new RuntimeException("Failed to fetch data from Nominatim API. Response code: " + responseCode);
        }
    }
}
