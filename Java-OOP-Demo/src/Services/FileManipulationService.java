package Services;

import Adapter.LocalDateAdapter;
import Common.Commons;
import Common.Enums;
import Dtos.Car;
import Dtos.Truck;
import Dtos.Vehicles;
import Interfaces.IFileManipulationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class FileManipulationService implements IFileManipulationService {

    //needs to be mutable
    private Map<String, Vehicles> vehicleMap = new LinkedHashMap<>();
    private final Map<String, Vehicles> vehicleMapFromFile = new LinkedHashMap<>();
    boolean FileExists = false;

    public FileManipulationService(){
        //Prefill Map from file
        Path filePath = Paths.get(Commons.LibraryPath);
        if(filePath.getParent() != null){
            if (Files.exists(filePath)) {
                try {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())//custom adapter
                            .create();

                    try (Stream<String> lines = Files.lines(Path.of("file.txt"))) {
                        lines.forEach(line -> {
                            JsonObject FileLine = gson.fromJson(line, JsonObject.class);
                            String category =FileLine.get("category").getAsString();

                            //Get vehicles from a JSON string from file.
                            if (Objects.equals( Integer.parseInt(category), Enums.VehicleCategories.CAR.getCode())){
                                Car carInFile = gson.fromJson(FileLine, Car.class);
                                vehicleMapFromFile.putIfAbsent(carInFile.getPlateNumber(), carInFile);
                            }

                            if (Objects.equals(Integer.parseInt(category), Enums.VehicleCategories.TRUCK.getCode())){
                                Truck truckInFile = gson.fromJson(FileLine, Truck.class);
                                vehicleMapFromFile.putIfAbsent(truckInFile.getPlateNumber(), truckInFile);
                            }
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                FileExists = true;
            }
        }
    }

    public <T extends Vehicles> void ImportJSONToFile(String JSONText, Class<T> vehicleCLass){
        //Pre
        //AddVehiclesList(JSONText, vehicleCLass, false);
        Path filePath = Paths.get(Commons.LibraryPath);

        //Validity checks by file exists and entry exists
        if(FileExists)
            ;
        else
            CreateFile(JSONText);

        if (Files.exists(filePath)) {
                //Fills textFile;
            }

    }

    //Create textFile if not exist
    private void CreateFile(String JSONText){
        try (FileWriter writer = new FileWriter(Commons.LibraryPath)){
            writer.write(JSONText);
            System.out.println("Vehicle library created!");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean IsFileValid(){
        return true;
    }

    public Map<String, Vehicles> LoadMapByFile(){
        //Load Map with textFile entries
        Map<String, Vehicles> vehicleMap = new HashMap<>();

        return new HashMap<>();
    }

    private boolean IsFileExist(){
        return true;
    }

    //region Vehicle private Methods.
    private <T extends Vehicles> void AddVehiclesList(String jsonVehiclesList, Class<T> vehicleCLass, boolean isFromFile)
    {
        //Simulate the imported cars from a JSON string.
        Type jsonType = TypeToken.getParameterized(List.class, vehicleCLass).getType();
        List<T> importedVehicles = parseJsonList(jsonVehiclesList, jsonType);

        for(T vehicle:importedVehicles)
        {
            if(vehicleCLass == Car.class)
                AddCar(vehicle.getPlateNumber(), vehicle.getMake(), vehicle.getModel(), vehicle.getRegisteredDate(), isFromFile);
            else if(vehicleCLass == Truck.class)
                AddVan(vehicle.getPlateNumber(), vehicle.getMake(), vehicle.getModel(), vehicle.getRegisteredDate(), isFromFile);
        }
    }

    private <T> List<T> parseJsonList(String json, Type type) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())//make use of the custom adapter to handle LocalDate deserialization
                .create();
        return gson.fromJson(json, type);
    }

    private Car AddCar(String plateNumber, String make, String model, LocalDate registeredDate, boolean isFromFile) {
        Car newCar = new Car(plateNumber,make,model,registeredDate);

        if(isFromFile)
            vehicleMapFromFile.putIfAbsent(newCar.getPlateNumber(), newCar);
        else
            vehicleMap.putIfAbsent(newCar.getPlateNumber(), newCar);

        return newCar; //Return for adding into existingVehicles
    }

    private Truck AddVan(String plateNumber, String make, String model, LocalDate registeredDate, boolean isFromFile) {
        Truck newVan = new Truck(plateNumber, make,model,registeredDate);
        if(isFromFile)
            vehicleMapFromFile.putIfAbsent(newVan.getPlateNumber(), newVan);
        else
            vehicleMap.putIfAbsent(newVan.getPlateNumber(), newVan);

        return newVan; //Return for adding into existingVehicles
    }
    //endregion
}
