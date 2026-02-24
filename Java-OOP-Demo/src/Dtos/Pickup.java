package Dtos;

import Common.Enums;

import java.time.LocalDate;

public class Pickup extends Vehicles{
    public Pickup(String plateNumber, String make, String model, LocalDate registeredDate, boolean available) {
        super(plateNumber, make, model, registeredDate, available, Enums.VehicleCategories.PICKUP.getCode());
    }
}
