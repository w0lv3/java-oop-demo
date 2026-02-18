package Interfaces;

import Dtos.Vehicles;

import java.util.Map;

public interface IFileManipulationService {
    <T extends Vehicles> void ImportJSONToFile(String JSONText, Class<T> vehicleCLass);
    Map<String, Vehicles> LoadMapByFile();
}
