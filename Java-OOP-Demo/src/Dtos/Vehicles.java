package Dtos;

import Interfaces.IRental;

import java.time.LocalDate;
import java.util.Objects;

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

    //Purpose is to have the equals of vehicle to focus on the
    // plate number that is unique instead of the whole vehicle object.
    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if (!(o instanceof Vehicles))
            return false;

        Vehicles inputVehicle = (Vehicles) o;
        return this.plateNumber.equals(inputVehicle.plateNumber);
    }

    //Have the hasCode of the vehicle to take the hasCode of the plate number.
    @Override
    public int hashCode()
    {
        return Objects.hash(this.plateNumber);
    }


}
