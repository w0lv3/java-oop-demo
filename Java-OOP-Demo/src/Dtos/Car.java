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

    //Daily rates section
    // use java use an annotation instead of having the property signature as such using
    // "public override double getDailyRate()" like C#
    @Override
    public double getDailyRate() {

        double DailyPrice = 450;//Basic price
        Enums.VehicleBrands vehicleBrand = Enums.VehicleBrands
                .getVehicleCategoriesByDescription(getMake().toLowerCase());
        vehicleBrand = vehicleBrand != null ? vehicleBrand : Enums.VehicleBrands.NONE;

        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case Enums.VehicleBrands.TOYOTA:
                DailyPrice = 225;
            case Enums.VehicleBrands.FORD:
                DailyPrice = 300;
            case Enums.VehicleBrands.NISSAN:
                DailyPrice = 125;
        }

        //decrease based of the machine year (older => less expensive)
        int carYear = LocalDate.now().getYear() - getRegisteredDate().getYear();
        DailyPrice -= DailyPrice / carYear; // -= decrease the by year value into daily rate.

        return DailyPrice;
    }

    private boolean noPromo = false;
    @Override
    public double getPromoPrice() {
        double promoPrice = getDailyRate();
        Enums.VehicleBrands vehicleBrand = Enums.VehicleBrands
                .getVehicleCategoriesByDescription(getMake().toLowerCase());

        vehicleBrand = vehicleBrand != null ? vehicleBrand : Enums.VehicleBrands.NONE;
        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case Enums.VehicleBrands.TOYOTA:
                promoPrice -= (promoPrice * .5); //cheapest
                break;
            case Enums.VehicleBrands.FORD:
                promoPrice -= (promoPrice * .3); //cheaper
                break;
            default:
                noPromo = true;
                break;
        }

        return promoPrice;
    }

    public boolean getNoPromo(){ return noPromo; }

}
