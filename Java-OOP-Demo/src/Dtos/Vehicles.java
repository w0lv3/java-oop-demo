package Dtos;

import Common.Enums;
import Interfaces.IRentalService;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Vehicles {
    private final String plateNumber;
    private final String make;
    private final String model;
    private final LocalDate registeredDate;
    private final int category;
    private final boolean available;
    private transient boolean noPromo;

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
    } //Should set to enum type instead?

    public boolean getAvailability(){return available;}
    public boolean getNoPromo(){return noPromo;}

    public Vehicles(String plateNumber, String make, String model, LocalDate registeredDate, boolean available, int category) {

        //Validation check
        if(plateNumber.isBlank()) //isBlank() => .IsNullOrWhiteSpace in c#
            throw new IllegalArgumentException("Make cannot be null");
        if(make.isBlank())
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
        this.available = available;
    }

    //Setter
    public void setNoPromo(boolean noPromo){
        this.noPromo = noPromo;
    }

    //String.format("%.2f", getPromoPrice()) for 2 decimal points
    @Override
    public String toString() {
        return getMake() + " | " + getModel() + " | " + getRegisteredDate();
    }

    //Print for rentals
    public String rentalDisplay(){
        return getPlateNumber() + " | "
                + Enums.VehicleCategories.getByCode(getCategory()).getDescription() + " | "
                + getMake() + " | "
                + getModel() + " | "
                + getRegisteredDate();
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
