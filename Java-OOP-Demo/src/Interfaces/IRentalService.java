package Interfaces;

import Dtos.Vehicles;

public interface IRentalService {

    // Polymorphic behavior by using the getRentalPrice from its extensions
    void DisplayAvailableVehicles();
    double GetSelectedTotalCost(Vehicles selectedVehicle, int days);
}
