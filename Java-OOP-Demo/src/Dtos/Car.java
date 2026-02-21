package Dtos;

import Common.Enums;

import java.time.LocalDate;

public class Car extends Vehicles {
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
                .getByDescription(getMake().toLowerCase());
        vehicleBrand = vehicleBrand != null ? vehicleBrand : Enums.VehicleBrands.NONE;

        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case TOYOTA:
                DailyPrice = 225;
                break;
            case FORD:
                DailyPrice = 300;
                break;
            case NISSAN:
                DailyPrice = 125;
                break;
        }

        //decrease based of the machine year (older => less expensive)
        //make use of Math.max to avoid divide by 0 for an entry existing in the same year of now.
        int carYear = Math.max(1,(LocalDate.now().getYear() - getRegisteredDate().getYear()));
        DailyPrice -= DailyPrice / carYear; // -= decrease the by year value into daily rate.

        return DailyPrice;
    }

    private transient boolean noPromo = false;
    @Override
    public double getPromoPrice() {
        double promoPrice = getDailyRate();
        Enums.VehicleBrands vehicleBrand = Enums.VehicleBrands
                .getByDescription(getMake().toLowerCase());

        vehicleBrand = vehicleBrand != null ? vehicleBrand : Enums.VehicleBrands.NONE;
        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case TOYOTA:
                promoPrice -= (promoPrice * .5); //cheapest
                break;
            case FORD:
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
