package Services;

import Adapter.LocalDateAdapter;
import Common.Commons;
import Common.Enums;
import Dtos.Car;
import Dtos.Truck;
import Dtos.Vehicles;
import Interfaces.IFileManipulationService;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;

public class FileManipulationService implements IFileManipulationService {

    //needs to be mutable
    private Map<String, Vehicles> vehicleMapFromFile = new LinkedHashMap<>();
    boolean FileExists = false;

    private final Path filePath = Paths.get(Commons.LibraryFilePath);
    private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())//make use of the custom adapter to handle LocalDate deserialization
                .create();

    public FileManipulationService(){
        if(filePath.getParent() != null){
            if (Files.exists(filePath)) {
                FileExists = true;
                LoadMapByFile();
            }
            try {
                //Create directory
                Files.createDirectories(Paths.get(Commons.LibraryDirectoryPath));
                System.out.println("library directory created!");
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public <T extends Vehicles> void ImportJSONToFile(String JSONText, Class<T> vehicleCLass){
        //Increment vehicleMapFromFile Map with JSON string
        AddVehiclesList(JSONText, vehicleCLass);
        UpdateFileWithMap(null); //Increment map to file.
    }

    //Cleans the JSON file to insert the updated vehicleMapFromFile map
    public void UpdateFileWithMap(Map<String, Vehicles> updatedVehicleMap){

        //Add and replace existing lines, in case of an update.
        if(updatedVehicleMap != null)
            vehicleMapFromFile = updatedVehicleMap;

        UpdateJsonFile();
    }

    private void UpdateJsonFile() {
        //Serialize the main directory of vehicles.
        try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)){
            vehicleMapFromFile.forEach((plateNumber, vehicle) -> {
                try {
                    writer.newLine();
                    writer.write(gson.toJson(vehicle));
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public Map<String, Vehicles> LoadMapByFile(){
        Map<String, Vehicles> filledMap = new LinkedHashMap<>();
        //Fill Map from file
        if (FileExists) {
            try {
                Files.lines(filePath)
                        .filter(line -> !line.isBlank())
                        .forEach(line -> {
                            JsonObject fileLine = gson.fromJson(line, JsonObject.class);
                            String category =fileLine.get("category").getAsString();

                            //Get vehicles from a JSON string from file.
                            if (Objects.equals( Integer.parseInt(category), Enums.VehicleCategories.CAR.getCode())){
                                Car carInFile = gson.fromJson(fileLine, Car.class);
                                filledMap.putIfAbsent(carInFile.getPlateNumber(), carInFile);
                            }

                            if (Objects.equals(Integer.parseInt(category), Enums.VehicleCategories.TRUCK.getCode())){
                                Truck truckInFile = gson.fromJson(fileLine, Truck.class);
                                filledMap.putIfAbsent(truckInFile.getPlateNumber(), truckInFile);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
            System.out.println("Vehicle library does not exist!");

        return filledMap;//returns empty if fill is empty.
    }

    //region add Vehicle private Methods.
    private <T extends Vehicles> void AddVehiclesList(String jsonVehiclesList, Class<T> vehicleCLass)
    {
        //Simulate the imported cars from a JSON string.
        Type jsonType = TypeToken.getParameterized(List.class, vehicleCLass).getType();
        List<T> importedVehicles = parseJsonList(jsonVehiclesList, jsonType);

        for(T vehicle:importedVehicles)
        {
            if(vehicleCLass == Car.class)
                AddCar(vehicle.getPlateNumber(), vehicle.getMake(), vehicle.getModel(), vehicle.getRegisteredDate());
            else if(vehicleCLass == Truck.class)
                AddVan(vehicle.getPlateNumber(), vehicle.getMake(), vehicle.getModel(), vehicle.getRegisteredDate());
        }
    }

    private <T> List<T> parseJsonList(String json, Type type) {
        return gson.fromJson(json, type);
    }

    private Car AddCar(String plateNumber, String make, String model, LocalDate registeredDate) {
        Car newCar = new Car(plateNumber,make,model,registeredDate);
        vehicleMapFromFile.putIfAbsent(newCar.getPlateNumber(), newCar);
        return newCar; //Return for adding into existingVehicles
    }

    private Truck AddVan(String plateNumber, String make, String model, LocalDate registeredDate) {
        Truck newVan = new Truck(plateNumber, make,model,registeredDate);
        vehicleMapFromFile.putIfAbsent(newVan.getPlateNumber(), newVan);
        return newVan; //Return for adding into existingVehicles
    }
    //endregion
}
