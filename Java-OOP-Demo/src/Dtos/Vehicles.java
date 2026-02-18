package Dtos;

import Interfaces.IRental;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Vehicles implements IRental {
    private final String plateNumber;
    private final String make;
    private final String model;
    private final LocalDate registeredDate;
    private final int category;

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

    public int getCategory(){
        return category;
    }

    public Vehicles(String plateNumber, String make, String model, LocalDate registeredDate, int category) {

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
        this.category = category;
    }

    //String.format("%.2f", getPromoPrice()) for 2 decimal points
    @Override
    public String toString() {
        return getMake() + " | " + getModel() + " | " + getRegisteredDate() + " | Daily rental price: $" + String.format("%.2f", getPromoPrice()) + (getNoPromo() ? " Promotional price not applied!" : "") ;
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

    //Return the total cost for rental.
    public double getTotalRate(Integer days) {
        return getPromoPrice() * days;
    }
}
