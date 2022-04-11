# Weather mobile application

## Table of Contents

- [About](#about)
- [Feauters](#Feauters)
- [Libraries](#Libraries)
- [Usage](#usage)

## About <a name = "about"></a>

- download and display the weather status and the temperature from your location.

- pick a specific location on the map and add it to a list of favorite locations and get all the weather information at this location.

- In addition you can set an alert for rain, wind, very low or very high temperature, fog, snow … etc 


<p align="right">(<a href="#top">back to top</a>)</p>

## Feauters

- Access to user data storage for recording application data and favorites (ROOM Database).

- Access to network for downloading data from open weather api (Retrofit third-party library).

- Access to application settings storage for recording default settings (Shared Preference).

- Google Maps Sdk and Locations Using GPS.

- Image downloading (Glide third-party library).

- Localization of the application's user visible text for (English - Arabic) language only.

- Notification. 

- CoroutineWorker -> (Work Manager).

- MVVM architectural design pattern.

<p align="right">(<a href="#top">back to top</a>)</p>

## Libraries

- weather forecasts from the open weather services [open weather](https://openweathermap.org/).
- ROOM Database
- Retrofit third-party library
- Shared Preference
- Google Maps Sdk 
- Glide third-party library

<p align="right">(<a href="#top">back to top</a>)</p>

## Usage <a name = "usage"></a>

### Home Screen:

This screen displays:
-  The current temperature
- Current date
- Current time
- Humidity
- Wind speed
- Pressure
- Clouds
- City
- Icon (suitable to the weather status)
- Weather description (clear sky, light rain... etc.)
- All the past hourly for the current date
- All past features for 7 days.

### Weather Alerts Screen:
This screen will contain a button to add weather alert which should set an alarm
with the following settings:
- The duration through which the alarm is active.
- The type of alarm even be just like notification or default alarm sound.
- Option to stop notification or turn off the alarm.

### Favorite Screen:
This screen lists the favorite locations. Pressing on an item should open another
screen that displays all the forecast information of this place.
In addition there should be a FAB button via which the user can add a new
favorite place. When it is clicked an activity whose map and auto-complete edit
text should be shown to give the user the ability to set a marker on a specific
location or type in the edit text the name of a city and save this location to the
favorites list.
Besides, there should be a facility to enable the user to remove a saved place

<p align="right">(<a href="#top">back to top</a>)</p>
