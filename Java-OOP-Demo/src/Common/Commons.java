package Common;

import java.time.format.DateTimeFormatter;

public class Commons {
    //dateFormatter across app.
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    //Library path
    public static final String LibraryDirectoryPath = "Java-OOP-Demo/src/Resources/Data";
    public static final String LibraryFilePath = "Java-OOP-Demo/src/Resources/Data/VehicleLibrary.json";
}
