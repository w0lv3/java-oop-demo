package Interfaces;

import Dtos.Vehicles;

public interface IVehicleManipulationService {

    void ShowAllVehicles();

    boolean SearchByPlate(String plate);

    Vehicles GetVehicleByPlate(String lookupPlate);

    void DeleteVehicleByPlate(String plateToDelete);

    void AddVehicleByPlate(String plateToUpdate, Vehicles vehicleToAdd);

    void UpdateVehicleByPlate(String plateToUpdate, Vehicles vehicleToApply);
}
