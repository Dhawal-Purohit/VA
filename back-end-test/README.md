# Virgin Atlantic ~ Flight Information Display

This project is a simple flight information display that reads flight data from a CSV and exposes a small REST API to query flights by date.

Important project configuration

- Java version (configured in `pom.xml`): 16
- Spring Boot parent: 2.5.12

How to build and test

From PowerShell (Windows):

```powershell
# Run unit tests
mvn clean test

# Or to force dependency updates and run tests
mvn -U clean test
```

Run the application

```powershell
# Run from Maven (dev mode)
mvn spring-boot:run

# Or build and run the jar
mvn -DskipTests package
java -jar target\back-end-test-1.0-SNAPSHOT.jar
```

API endpoints

The controller exposes the following endpoints (date format yyyy-MM-dd):

- GET /api/flights/{date}
- GET /api/flights?date={date}

If your application is deployed with a context-path (e.g. `/back-end-test`) the full URL becomes:

- http://localhost:8080/back-end-test/api/flights/2026-10-06

Examples (PowerShell/curl)

Path style:
```powershell
curl.exe -i http://localhost:8080/api/flights/2026-10-06
```

Query style:
```powershell
curl.exe -i "http://localhost:8080/api/flights?date=2026-10-06"
```

Behavior

- The service returns flights operating on the day of week for the provided date.
- Results are sorted by departure time ascending.
- Responses:
  - 200 OK with JSON list when flights are found
  - 204 No Content when no flights match
  - 400 Bad Request when the `date` parameter is missing or invalid (not yyyy-MM-dd)

Example response (for a Tuesday date). The API returns times formatted as HH:mm and days as DayOfWeek names:

```json
[
  {
    "departureTime": "09:00",
    "destination": "Antigua",
    "iata": "ANU",
    "flightNo": "VS033",
    "days": [
      "TUESDAY"
    ]
  },
  {
    "departureTime": "10:15",
    "destination": "Havana",
    "iata": "HAV",
    "flightNo": "VS063",
    "days": [
      "TUESDAY"
    ]
  },
  {
    "departureTime": "10:35",
    "destination": "Las Vegas",
    "iata": "LAS",
    "flightNo": "VS043",
    "days": [
      "MONDAY",
      "TUESDAY",
      "WEDNESDAY"
    ]
  },
  {
    "departureTime": "11:05",
    "destination": "Barbados",
    "iata": "BGI",
    "flightNo": "VS029",
    "days": [
      "SUNDAY",
      "MONDAY",
      "TUESDAY",
      "WEDNESDAY",
      "THURSDAY",
      "FRIDAY",
      "SATURDAY"
    ]
  },
  {
    "departureTime": "11:45",
    "destination": "Orlando",
    "iata": "MCO",
    "flightNo": "VS027",
    "days": [
      "SUNDAY",
      "TUESDAY"
    ]
  },
  {
    "departureTime": "12:20",
    "destination": "Cancun",
    "iata": "CUN",
    "flightNo": "VS093",
    "days": [
      "TUESDAY"
    ]
  },
  {
    "departureTime": "13:00",
    "destination": "Orlando",
    "iata": "MCO",
    "flightNo": "VS015",
    "days": [
      "SUNDAY",
      "MONDAY",
      "TUESDAY",
      "WEDNESDAY",
      "THURSDAY",
      "FRIDAY",
      "SATURDAY"
    ]
  },
  {
    "departureTime": "15:35",
    "destination": "Las Vegas",
    "iata": "LAS",
    "flightNo": "VS044",
    "days": [
      "SUNDAY",
      "MONDAY",
      "TUESDAY",
      "WEDNESDAY",
      "THURSDAY",
      "FRIDAY",
      "SATURDAY"
    ]
  }
]
```

Notes

- Ensure your runtime JDK matches the project configuration (Java 16). If you run with a much newer JDK you may encounter compatibility issues with some test/mock tooling.
- If you run into dependency resolution problems, run `mvn -U clean package` to force updates.
