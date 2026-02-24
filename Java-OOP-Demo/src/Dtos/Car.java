package Dtos;

import Common.Enums;

import java.time.LocalDate;

public class Car extends Vehicles {
    //Extends is the inheritance in C#
    //Example in C# => public class Car : Vehicles
    //Use Implements to chain in an interface
    //Example in C# => public class Car : Vehicles, IInsurance

    public Car (String plateNumber, String make, String model, LocalDate registeredDate, boolean available) {
        super (plateNumber, make, model, registeredDate, available, Enums.VehicleCategories.CAR.getCode());
        // super represent base in C#
        // Example public Car (String make, String model, LocalDate registeredDate) : base (make, model, registeredDate);
    }
}
