package org.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp {

    private static final String API_BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) {
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Print Temperature");
            System.out.println("2. Print Wind Speed");
            System.out.println("3. Print Pressure");
            System.out.println("0. Exit");

            String choice = readInput("Enter your choice: ");
            switch (choice) {
                case "1":
                    printWeatherData("temp");
                    break;
                case "2":
                    printWeatherData("wind.speed");
                    break;
                case "3":
                    printWeatherData("pressure");
                    break;
                case "0":
                    System.out.println("Exiting the program.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printWeatherData(String dataField) {
        String date = readInput("Enter the date (YYYY-MM-DD): ");
        String apiUrl = API_BASE_URL;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray hourlyForecasts = jsonResponse.getJSONArray("list");

            for (int i = 0; i < hourlyForecasts.length(); i++) {
                JSONObject forecast = hourlyForecasts.getJSONObject(i);
                String dt_txt = forecast.getString("dt_txt").split(" ")[0];

                if (dt_txt.equals(date)) {
                    JSONObject mainData = forecast.getJSONObject("main");
                    double value = mainData.getDouble(dataField);
                    System.out.println(dataField + " on " + date + ": " + value);
                    return;
                }
            }

            System.out.println("No data found for the provided date.");

        } catch (IOException e) {
            System.out.println("Error fetching data from API: " + e.getMessage());
        }
    }

    private static String readInput(String prompt) {
        System.out.print(prompt);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
            return "";
        }
    }
}
