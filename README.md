# Weather App

## Overview

The **Weather App** is a Java-based desktop application that provides real-time weather information for user-specified locations. It features a graphical user interface (GUI) built with Swing and integrates with the Open-Meteo API to display weather details, including temperature, humidity, wind speed, and condition-specific icons.

---

## Features

- **Dynamic Weather Search**: Enter a location to retrieve real-time weather data.
- **Comprehensive Weather Details**:
  - Temperature in Fahrenheit.
  - Humidity and wind speed.
  - Weather conditions (Clear, Cloudy, Rain, Snow).
- **Visual Representation**: Displays condition-specific icons dynamically.
- **Error Handling**: Manages invalid inputs gracefully.

---

## Project Structure

### 1. `AppLauncher.java`
- **Role**: Entry point for the application.
- **Details**:
  - Invokes the `WeatherAppGui` class to launch the GUI.

### 2. `WeatherAppGui.java`
- **Role**: Provides the graphical user interface for the application.
- **Details**:
  - Accepts user input for location searches.
  - Displays weather data fetched via the `WeatherApp` backend.
  - Includes interactive features such as buttons and labels for real-time updates.

### 3. `WeatherApp.java`
- **Role**: Handles backend logic and API interactions.
- **Details**:
  - Retrieves geolocation data for user-specified locations.
  - Fetches weather details using Open-Meteo API.
  - Parses and processes API responses for the GUI display.

---

## Prerequisites

### Tools and Software
- **Java Development Kit (JDK)**: Version 8 or higher.
- **Libraries**:
  - JSON Simple Library (`json-simple-1.1.1.jar`) for handling API responses.

### Required Assets
- Place the following image files in the `src/assets/` directory:
  - `clear.png`
  - `cloudy.png`
  - `rain.png`
  - `snow.png`
  - `humidity.png`
  - `windspeed.png`
  - `search.png`

 ---
## Usage

1. Launch the application by running the `AppLauncher` class.
2. Enter a location name (e.g., "Los Angeles") into the search bar.
3. Click the search button to fetch the weather.
4. View the displayed details:
   - **Temperature**: Displayed in Fahrenheit.
   - **Weather Condition**: Clear, Cloudy, Rain, or Snow.
   - **Humidity**: Displayed as a percentage.
   - **Wind Speed**: Displayed in mp/h.
   - **Dynamic Icons**: Reflect the current weather conditions.

---
## Downloads:
1. Simple JSON Jar: https://code.google.com/archive/p/json-simple/downloads
2. Weather API: https://open-meteo.com/en/docs#latitude=33.767&longitude=-118.1892
3. Weather Icons: https://www.dropbox.com/scl/fi/wl5mfpt0fwyol14ku0jxu/weatherapp_images.rar?rlkey=oknkxq85slvx59yyw7iyogvm9&e=1&dl=0
4. GeoLocation API: https://open-meteo.com/en/docs/geocoding-api
