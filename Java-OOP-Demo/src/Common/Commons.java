package Common;

import java.time.format.DateTimeFormatter;

public class Commons {
    //dateFormatter across app.
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    //Library path
    public static final String LibraryDirectoryPath = "Java-OOP-Demo/src/Resources/Data";
    public static final String LibraryFilePath = "Java-OOP-Demo/src/Resources/Data/VehicleLibrary.json";
    //Raw JSON Car items
    public final static String jsonCarList =
            """
            [
                {"plateNumber":"QB90JA20", "make":"Toyota", "model":"Camry", "registeredDate":"01-01-2020","category":1, "available": false},
                {"plateNumber":"VA32JA18","make":"Ford", "model":"F-150", "registeredDate":"05-03-2018", "category":1, "available": true}
            ]
            """;

    //Raw JSON Vans items
    public final static String jsonVanList =
            """
            [
                {"plateNumber":"VN33MA97","make":"Renault", "model":"Trafic", "registeredDate":"01-02-2005","category":3, "available": true},
                {"plateNumber":"VN32MY15","make":"Nissan", "model":"NV200", "registeredDate":"15-10-2008","category":3, "available": false}
            ]
            """;

    //Raw JSON Vans items
    public final static String jsonPickupList =
            """
            [
                {"plateNumber":"PL13MA98","make":"Toyota", "model":"Hilux", "registeredDate":"01-03-1998","category":3, "available": true},
                {"plateNumber":"RH32MY15","make":"Nissan", "model":"Triton", "registeredDate":"15-05-2015","category":3, "available": true}
            ]
            """;

    //Raw JSON Vans items
    public final static String jsonSUVList =
            """
            [
                {"plateNumber":"PG29MA98","make":"Suzuki", "model":"Across", "registeredDate":"05-01-2026","category":4, "available": true},
                {"plateNumber":"TH42MY15","make":"Honda", "model":"CRV", "registeredDate":"15-05-2023","category":4, "available": false}
            ]
            """;
}
