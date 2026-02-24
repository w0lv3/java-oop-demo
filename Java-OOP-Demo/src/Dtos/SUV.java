package Dtos;

import Common.Enums;

import java.time.LocalDate;

public class SUV extends Vehicles{
    public SUV(String plateNumber, String make, String model, LocalDate registeredDate, boolean available) {
        super(plateNumber, make, model, registeredDate, available, Enums.VehicleCategories.SUV.getCode());
    }
}
