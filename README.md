- Implemented with SpringBoot with the MVC

- JDK 11, Spring Boot 2.4.5, Gradle

- Update application-local.xml before build
  > weather-stack-access-key, the access key of weather stack
  > open-weather-map-access-key, the api key of open weather map
  > cache-expiry, the result would be cached in X seconds and the result would be retrieved from cache

- Compile the program with "gradlew bootJar"

- Start the server with "jar -jar currencyfair-weather-backend.jar"

- Test with PostMan
  Sample
  
  By default, "Melbourne" from Australia would be retrieved
  http://localhost:8080/weather/checkWeather
  
  The parameter city could be posted to retrieve the value, the ",Country" is optional
  http://localhost:8080/weather/checkWeather?city=Melbourne,AU
  
  http://localhost:8080/weather/checkWeather?city=London,UK
  
  http://localhost:8080/weather/checkWeather?city=Hong Kong

  
