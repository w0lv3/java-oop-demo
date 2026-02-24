package Dtos;

import Common.Enums;

import java.time.LocalDate;

public class Van extends Vehicles {

    public Van(String plateNumber, String make, String model, LocalDate registeredDate, boolean available){
        super (plateNumber, make, model, registeredDate, available, Enums.VehicleCategories.VAN.getCode());
    }
}
