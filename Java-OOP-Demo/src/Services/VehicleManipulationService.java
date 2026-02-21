package Services;

import Common.Enums;
import Common.Exceptions;
import Dtos.Car;
import Dtos.Truck;
import Dtos.Vehicles;

import Interfaces.IFileManipulationService;
import Interfaces.IVehicleManipulationService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class VehicleManipulationService implements IVehicleManipulationService {

    private final Map<String, Vehicles> vehicleMap = new HashMap<>();
    private final List<Vehicles> allVehicles = new ArrayList<>();
    //Load up FileService
    IFileManipulationService FileManipulationService = new FileManipulationService();

    public VehicleManipulationService(String jsonCarList, String jsonVanList){
        //Stack vs heap
        String stackVariable = "vehicle";
        Car heapVariable = new Car("1010JA90","Toyota", "Rav4", LocalDate.of(1990,1,31));
        System.out.printf("\nThis a stack : %s",stackVariable );
        System.out.printf("\nThis a heap : %s\n",heapVariable);

        //Populate File with hardcoded JSON
        FileManipulationService.ImportJSONToFile(jsonCarList, Car.class);
        FileManipulationService.ImportJSONToFile(jsonVanList, Truck.class);
        //Load up MapWith text file needs to add validity for null ref
        FileManipulationService.LoadMapByFile().forEach(this::AddVehicleByPlate);

        //Fill allVehicles to be used in the simple operation below
        //to demonstrate streaming, group by, peek, foreach and so on.
        //region simple examples
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
        //endregion
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
        if(lookupVehicle == null)
            throw new Exceptions.VehicleNotFoundException(lookupPlate);//Custom exception

        System.out.println("Vehicle Found: +" + lookupVehicle);
        return lookupVehicle;
    }

    public void DeleteVehicleByPlate(String plateToDelete){
        vehicleMap.remove(plateToDelete); //plate is key in the Map
        //Update file
        FileManipulationService.UpdateFileWithMap(vehicleMap);
    }

    public void AddVehicleByPlate(String plateToUpdate, Vehicles vehicleToAdd) {
        vehicleMap.putIfAbsent(plateToUpdate, vehicleToAdd); //Add vehicle to map if not exist
        //Update file
        FileManipulationService.UpdateFileWithMap(vehicleMap);
    }

    public void UpdateVehicleByPlate(String plateToUpdate, Vehicles vehicleToApply) {
        //Update existing vehicle in map with new value;
        vehicleMap.computeIfPresent(plateToUpdate, (k,v)-> vehicleToApply);
        //Update file
        FileManipulationService.UpdateFileWithMap(vehicleMap);
    }
}
