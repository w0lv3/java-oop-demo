package Services;

import Dtos.Vehicles;

import Interfaces.IVehicleManipulationByMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleManipulationByMap implements IVehicleManipulationByMap {

    Map<String, Vehicles> vehicleMap = new HashMap<>();
    public VehicleManipulationByMap(List<Vehicles> allVehicle){
        //Load up map
        allVehicle.forEach(v-> {
            vehicleMap.put(v.getPlateNumber(), v);
        });
    }

    public void ShowAllVehicles() {
        vehicleMap.forEach((k,vehicle)-> {
            System.out.println(vehicle);
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

}
