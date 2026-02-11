package Dtos;

import Interfaces.IRental;

import java.time.LocalDate;

public abstract class Vehicles implements IRental {
    private  final String plateNumber;
    private final String make;
    private final String model;
    private final LocalDate registeredDate;

    //Getters
    public String getPlateNumber(){return plateNumber;}

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public Vehicles(String plateNumber, String make, String model, LocalDate registeredDate) {

        //Validation check
        if(make.isBlank()) //isBlank() => .IsNullOrWhiteSpace in c#
            throw new IllegalArgumentException("Make cannot be null");
        if(model.isBlank())
            throw new IllegalArgumentException("Model cannot be null");
        if(registeredDate == null)
            throw new IllegalArgumentException("Registered date cannot be null");
        if (registeredDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Registered date cannot be in the future");

        this.plateNumber = plateNumber;
        this.make = make;
        this.model = model;
        this.registeredDate = registeredDate;
    }

    // Polymorphic behavior by using the getCategory from its extensions
    public abstract String getCategory();

    @Override
    public String toString() {
        return getMake() + " " + getModel() + " " + getRegisteredDate() + " Rental price: $" + getRentalPrice();
    }
}
