package Dtos;

import Common.Enums;
import Interfaces.IRental;

import java.time.LocalDate;

public class Car extends Vehicles implements IRental {
    //Extends is the inheritance in C#
    //Example in C# => public class Car : Vehicles
    //Use Implements to chain in an interface
    //Example in C# => public class Car : Vehicles, IInsurance

    public Car (String plateNumber, String make, String model, LocalDate registeredDate) {
        super (plateNumber, make, model, registeredDate, Enums.VehicleCategories.CAR.getCode());
        // super represent base in C#
        // Example public Car (String make, String model, LocalDate registeredDate) : base (make, model, registeredDate);
    }

    //Insurance section
    // use java use an annotation instead of having the property signature as such using
    // "public override double getRentalPrice()" like C#
    private boolean noPromo = false;
    @Override
    public double getRentalPrice() {
        double insuranceBaseCost = 500;

        //Increment based of the machine year (older => more expensive)
        int carYear = LocalDate.now().getYear() - getRegisteredDate().getYear();
        insuranceBaseCost += carYear * 10; // += increment the by year value into premium base

        String Check = getMake().toLowerCase();
        switch (getMake().toLowerCase()) { //set to lowercase to avoid Case typo issues
            case "toyota":
                insuranceBaseCost -= (insuranceBaseCost * .5); //cheapest
                break;
            case "ford":
                insuranceBaseCost -= (insuranceBaseCost * .3); //cheaper
                break;
            default:
                noPromo = true;
                break;
        }

        return insuranceBaseCost;
    }

    public boolean getNoPromo(){
        return noPromo;
    }
}
