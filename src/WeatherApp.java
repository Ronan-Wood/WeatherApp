import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {

    // Get weather data for the specified location
    public static JSONObject getWeatherData(String Location) {
        // Get the location coordinates using the geolocation API
        JSONArray locationData = getLocationData(Location);

        // Initialize variables to store latitude and longitude
        double latitude = 0;
        double longitude = 0;

        // If the location data is not null or empty, extract the coordinates
        if (locationData != null && !locationData.isEmpty()) {
            JSONObject location = (JSONObject) locationData.get(0);
            latitude = (double) location.get("latitude");
            longitude = (double) location.get("longitude");
        } else {
            System.out.println("Location data is null or empty. Please check the location input.");
            return null;
        }

        // Build the URL for the weather API using the coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&temperature_unit=fahrenheit" +
                "&wind_speed_unit=mph&precipitation_unit=inch";

        try {
            // Fetch API response
            HttpURLConnection connection = fetchApiResponse(urlString);

            // Check if the connection is successful (200 = success)
            if (connection == null || connection.getResponseCode() != 200) {
                System.out.println("Failed to fetch weather data. HTTP error code: " + connection.getResponseCode());
                return null;
            }

            // Store the API response in a StringBuilder
            StringBuilder resultJson = new StringBuilder();
            Scanner scan = new Scanner(connection.getInputStream());
            while (scan.hasNext()) {
                resultJson.append(scan.nextLine());
            }
            scan.close(); // Close the scanner
            connection.disconnect(); // Disconnect the connection

            // Parse the JSON string into a JSONObject
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(resultJson.toString());

            // Check if the "hourly" data exists in the API response
            if (!resultJsonObj.containsKey("hourly")) {
                System.out.println("Hourly data is missing in the API response.");
                return null;
            }
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            // Get the list of times and find the current time index
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);
            if (index == 0) {
                System.out.println("Current time not found in the time list. Defaulting to the first index.");
            }

            // Get the temperature data
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            Number temperatureNumber = (Number) temperatureData.get(index); // Handle both Double and Long
            double temperature = temperatureNumber.doubleValue();

            // Get the weather code data
            JSONArray weatherCodeData = (JSONArray) hourly.get("weather_code");
            if (weatherCodeData == null) {
                System.out.println("weather_code is missing in the API response.");
                return null;
            }
            Number weatherCode = (Number) weatherCodeData.get(index); // Handle both Double and Long
            String weatherCondition = convertWeatherCode(weatherCode.longValue());

            // Get the humidity data
            JSONArray relativeHumidityData = (JSONArray) hourly.get("relative_humidity_2m");
            Number humidityNumber = (Number) relativeHumidityData.get(index); // Handle both Double and Long
            long humidity = humidityNumber.longValue();

            // Get the wind speed data
            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            Number windSpeedNumber = (Number) windSpeedData.get(index); // Handle both Double and Long
            double windSpeed = windSpeedNumber.doubleValue();

            // Create a JSON object to store the weather data for the GUI
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windSpeed);

            // Return the JSON object with the weather data
            return weatherData;

        } catch (Exception e) {
            // Print error details if an exception occurs
            System.out.println("Error processing weather data: " + e.getMessage());
            e.printStackTrace();
        }

        // Return null if there was an error
        return null;
    }

    // Get the location coordinates from the geolocation API
    public static JSONArray getLocationData(String Location) {
        // Replace spaces with "+" to match the API's expected input format
        Location = Location.replace(" ", "+");

        // Build the geolocation API URL
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                Location + "&count=10&language=en&format=json";

        try {
            // Fetch API response
            HttpURLConnection connection = fetchApiResponse(urlString);

            // Check if the connection is successful (200 = success)
            if (connection == null || connection.getResponseCode() != 200) {
                System.out.println("Failed to fetch location data. HTTP error code: " + connection.getResponseCode());
                return null;
            }

            // Store the API response in a StringBuilder
            StringBuilder resultJson = new StringBuilder();
            Scanner scan = new Scanner(connection.getInputStream());

            while (scan.hasNextLine()) {
                resultJson.append(scan.nextLine());
            }
            scan.close(); // Close the scanner
            connection.disconnect(); // Disconnect the connection

            // Parse the JSON string into a JSONObject
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(resultJson.toString());

            // Get the "results" array from the JSON object
            JSONArray locationData = (JSONArray) resultJsonObj.get("results");
            return locationData;

        } catch (Exception e) {
            // Print error details if an exception occurs
            System.out.println("Error processing location data: " + e.getMessage());
            e.printStackTrace();
        }

        // Return null if there was an error
        return null;
    }

    // Fetch API response from the specified URL
    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            // Create a URL object and open an HTTP connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Connect to the API
            connection.connect();
            return connection;

        } catch (Exception e) {
            // Print error details if an exception occurs
            System.out.println("Error connecting to API: " + e.getMessage());
            e.printStackTrace();
        }

        // Return null if the connection failed
        return null;
    }

    // Find the index of the current time in the list of times
    private static int findIndexOfCurrentTime(JSONArray timeList) {
        // Get the current time formatted to match the API's format
        String currentTime = getCurrentTime();

        // Loop through the time list to find the current time
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                return i; // Return the index if a match is found
            }
        }

        // Return 0 if the current time is not found
        return 0;
    }

    // Get the current time formatted to match the API's expected format
    public static String getCurrentTime() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the current time as "yyyy-MM-dd'T'HH:00"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        return currentDateTime.format(formatter);
    }

    // Convert a weather code to a human-readable weather condition
    private static String convertWeatherCode(long weatherCode) {
        String weatherCondition = "";

        // Map weather codes to human-readable conditions
        if (weatherCode == 0L) {
            weatherCondition = "Clear";
        } else if (weatherCode > 0L && weatherCode < 3L) {
            weatherCondition = "Cloudy";
        } else if ((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
            weatherCondition = "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {
            weatherCondition = "Snow";
        }

        // If the weather condition is not recognized, default to "Unknown"
        if (weatherCondition.isEmpty()) {
            weatherCondition = "Unknown";
        }

        return weatherCondition;
    }
}