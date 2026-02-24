package Interfaces;

import Dtos.Vehicles;

import java.util.Map;
import java.util.stream.Stream;

public interface IFileManipulationService {
    <T extends Vehicles> void ImportJSONToFile(String JSONText, Class<T> vehicleCLass);

    void UpdateFileWithMap(Map<String, Vehicles> incomingVehicles);

    Map<String, Vehicles> LoadMapByFile();

    Stream<Vehicles> GetAvailableVehiclesFromFile();

    Stream<Vehicles> GetRentedVehiclesFromFile();

}
