package Interfaces;

import Dtos.Vehicles;

import java.util.Map;

public interface IFileManipulationService {
    <T extends Vehicles> void ImportJSONToFile(String JSONText, Class<T> vehicleCLass);

    void UpdateFileWithMap(Map<String, Vehicles> incomingVehicles);

    Map<String, Vehicles> LoadMapByFile();
}
