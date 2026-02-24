package Services;

import Adapter.LocalDateAdapter;
import Common.Commons;
import Common.Enums;
import Dtos.*;
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
import java.util.stream.Stream;

public class FileManipulationService implements IFileManipulationService {

    //needs to be mutable
    private Map<String, Vehicles> vehicleMapFromFile = new LinkedHashMap<>();
    boolean FileExists = false;

    private final Path filePath = Paths.get(Commons.LibraryFilePath);
    private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())//make use of the custom adapter to handle LocalDate deserialization
                .create();

    public FileManipulationService(){
        if(filePath.getParent() != null) {
            if (Files.exists(filePath)) {
                FileExists = true;
                vehicleMapFromFile = LoadMapByFile();
            }
        }
        else{
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
        if(updatedVehicleMap != null){
            this.vehicleMapFromFile.clear(); //Clear the Map before putAll to handle deleted rows
            this.vehicleMapFromFile.putAll(updatedVehicleMap);
        }
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

                            if (Objects.equals(Integer.parseInt(category), Enums.VehicleCategories.VAN.getCode())){
                                Van VanInFile = gson.fromJson(fileLine, Van.class);
                                filledMap.putIfAbsent(VanInFile.getPlateNumber(), VanInFile);
                            }

                            if (Objects.equals(Integer.parseInt(category), Enums.VehicleCategories.PICKUP.getCode())){
                                Pickup pickupInFile = gson.fromJson(fileLine, Pickup.class);
                                filledMap.putIfAbsent(pickupInFile.getPlateNumber(), pickupInFile);
                            }

                            if (Objects.equals(Integer.parseInt(category), Enums.VehicleCategories.SUV.getCode())){
                                SUV SUVInFile = gson.fromJson(fileLine, SUV.class);
                                filledMap.putIfAbsent(SUVInFile.getPlateNumber(), SUVInFile);
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

    public Stream<Vehicles> GetAvailableVehiclesFromFile(){
        return this.vehicleMapFromFile.values().stream().filter(Vehicles::getAvailability);
    }

    public Stream<Vehicles> GetRentedVehiclesFromFile(){
        return this.vehicleMapFromFile.values().stream().filter(vehicle-> !vehicle.getAvailability());
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
                addCar(vehicle.getPlateNumber(), vehicle.getMake(), vehicle.getModel(), vehicle.getRegisteredDate(), vehicle.getAvailability());
            if(vehicleCLass == Van.class)
                addVan(vehicle.getPlateNumber(), vehicle.getMake(), vehicle.getModel(), vehicle.getRegisteredDate(), vehicle.getAvailability());
            if(vehicleCLass == Pickup.class)
                addPickup(vehicle.getPlateNumber(), vehicle.getMake(), vehicle.getModel(), vehicle.getRegisteredDate(), vehicle.getAvailability());
            else if(vehicleCLass == SUV.class)
                addSUV(vehicle.getPlateNumber(), vehicle.getMake(), vehicle.getModel(), vehicle.getRegisteredDate(), vehicle.getAvailability());
        }
    }

    private <T> List<T> parseJsonList(String json, Type type) {
        return gson.fromJson(json, type);
    }

    private Car addCar(String plateNumber, String make, String model, LocalDate registeredDate, boolean available) {
        Car newCar = new Car(plateNumber,make,model,registeredDate, available);
        vehicleMapFromFile.putIfAbsent(newCar.getPlateNumber(), newCar);
        return newCar; //Return for adding into existingVehicles
    }

    private Van addVan(String plateNumber, String make, String model, LocalDate registeredDate, boolean available) {
        Van newVan = new Van(plateNumber, make,model,registeredDate, available);
        vehicleMapFromFile.putIfAbsent(newVan.getPlateNumber(), newVan);
        return newVan; //Return for adding into existingVehicles
    }

    private Pickup addPickup(String plateNumber, String make, String model, LocalDate registeredDate, boolean available) {
        Pickup newVan = new Pickup(plateNumber, make,model,registeredDate, available);
        vehicleMapFromFile.putIfAbsent(newVan.getPlateNumber(), newVan);
        return newVan; //Return for adding into existingVehicles
    }

    private SUV addSUV(String plateNumber, String make, String model, LocalDate registeredDate, boolean available) {
        SUV newVan = new SUV(plateNumber, make,model,registeredDate, available);
        vehicleMapFromFile.putIfAbsent(newVan.getPlateNumber(), newVan);
        return newVan; //Return for adding into existingVehicles
    }
    //endregion
}
