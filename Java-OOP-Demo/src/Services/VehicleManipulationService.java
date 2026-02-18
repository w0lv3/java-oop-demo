package Services;

import Adapter.LocalDateAdapter;
import Common.Enums;
import Dtos.Car;
import Dtos.Truck;
import Dtos.Vehicles;

import Interfaces.IFileManipulationService;
import Interfaces.IVehicleManipulationByMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class VehicleManipulationService implements IVehicleManipulationByMap {

    private final Map<String, Vehicles> vehicleMap = new HashMap<>();
    private final List<Vehicles> allVehicles = new ArrayList<>();
    //Load up FileService
    //private IFileManipulationService FileManipulationService = new FileManipulationService();


    public VehicleManipulationService(String jsonCarList, String jsonVanList){
        //Stack vs heap
        String stackVariable = "vehicle";
        Car heapVariable = new Car("1010JA90","Toyota", "Rav4", LocalDate.of(1990,1,31));
        System.out.printf("\nThis a stack : %s",stackVariable );
        System.out.printf("\nThis a heap : %s\n",heapVariable);

        //Load up vehicleMap
        AddVehiclesList(jsonCarList, Car.class);
        AddVehiclesList(jsonVanList, Truck.class);

        //Fill allVehicles
        vehicleMap.forEach((plateNumber,vehicles) ->allVehicles.add(vehicles) );

        //Display all vehicles
        DisplayAllVehicles();

        System.out.println("\nThe rental company have these cars: ");
        //Display all Cars only
        allVehicles.stream()
                .filter(v-> v.getCategory() == 1)
                .forEach(v-> System.out.println(v));

        System.out.println("\nand these trucks: ");
        //Display all Trucks only
        allVehicles.stream()
                .filter(v-> v.getCategory() == 2)
                .forEach(v-> System.out.println(v));

        //Display promo vehicles
        System.out.println("\nWe recommend the Toyota vehicles as there is a current promotion. The Toyota vehicles are: ");
        allVehicles.stream()
                .filter(v-> v.getMake().equals("Toyota"))
                .peek(v-> System.out.println(v.getCategory() + " " + v));

        // create a Map collection of vehicles grouped by the make
        System.out.println("Available vehicles by brand:");
        System.out.printf("\nWe have a total of %d vehicles.", allVehicles.size()); //.size() is like .Count() in C#
        Map<String, List<Vehicles>> vehiclesByMake = allVehicles.stream().collect(Collectors.groupingBy(v -> v.getMake()));
        vehiclesByMake.forEach((make, vehicles)-> System.out.printf("\n%d from the %s brand.", vehicles.size(), make));

        ////Populate File with hardcoded JSON
        //FileManipulationService.ImportJSONToFile(jsonCarList, Car.class);
        //FileManipulationService.ImportJSONToFile(jsonVanList, Truck.class);
        ////Load up MapWith text file needs to add validity for null ref
        //FileManipulationService.LoadMapByFile().forEach((plateNumber,vehicles) -> AddVehicleByPlate(plateNumber,vehicles));

    }

    private void DisplayAllVehicles() {
        System.out.println("\nAll vehicles that the rental company owns are: ");
        allVehicles.forEach(v-> System.out.println(v));
    }

    public void ShowAllVehicles() {
        TreeMap<Integer, List<Vehicles>> vehiclesByCategory = vehicleMap.values().stream().collect(Collectors
                .groupingBy(v -> v.getCategory(), ()->new TreeMap<>(), Collectors.toList()));

        System.out.println("\nCars: ");
        vehiclesByCategory.forEach((k,vehicles) -> {
            if(k == Enums.VehicleCategories.CAR.getCode())
                vehicles.forEach(v-> System.out.println(v));
        });

        System.out.println("\nTrucks: ");
        vehiclesByCategory.forEach((k,vehicles) -> {
            if(k == Enums.VehicleCategories.TRUCK.getCode())
                vehicles.forEach(v-> System.out.println(v));
        });
    }

    public boolean SearchByPlate(String plate) {
        if (vehicleMap.containsKey(plate)) { // containsKey(key)
            System.out.printf("Vehicle with plate number %s exists!\n", plate);
            return true;
        }
        System.out.println("Vehicle with plate number does not exists!");
        return false;
    }

    public Vehicles GetVehicleByPlate(String lookupPlate){
        Vehicles lookupVehicle = vehicleMap.get(lookupPlate); //Returns the found vehicle
        if(lookupVehicle != null)
            System.out.println("Vehicle Found: +" + lookupVehicle);
        else
            System.out.println("Vehicle not exist!");

        return lookupVehicle;
    }

    public void DeleteVehicleByPlate(String plateToDelete){
        vehicleMap.remove(plateToDelete); //plate is key in the Map
    }

    public void AddVehicleByPlate(String plateToUpdate, Vehicles vehicleToAdd) {
        vehicleMap.putIfAbsent(plateToUpdate, vehicleToAdd); //Add vehicle to map if not exist
    }

    public void UpdateVehicleByPlate(String plateToUpdate, Vehicles vehicleToApply) {
        //Update existing vehicle in map with new value;
        vehicleMap.computeIfPresent(plateToUpdate, (k,v)-> vehicleToApply);
    }

    //region Vehicle private Methods.
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())//make use of the custom adapter to handle LocalDate deserialization
                .create();
        return gson.fromJson(json, type);
    }

    private Car AddCar(String plateNumber, String make, String model, LocalDate registeredDate) {
        Car newCar = new Car(plateNumber,make,model,registeredDate);
        vehicleMap.putIfAbsent(newCar.getPlateNumber(), newCar);

        return newCar; //Return for adding into existingVehicles
    }

    private Truck AddVan(String plateNumber, String make, String model, LocalDate registeredDate) {
        Truck newVan = new Truck(plateNumber, make,model,registeredDate);
        vehicleMap.putIfAbsent(newVan.getPlateNumber(), newVan);

        return newVan; //Return for adding into existingVehicles
    }

}
