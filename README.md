- Implemented with SpringBoot with the MVC
- JDK 11, Spring Boot 2.4.5, Gradle
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

  