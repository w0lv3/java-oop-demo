package Dtos;

import Common.Enums;
import Interfaces.IRental;

import java.time.LocalDate;

public class Truck extends Vehicles implements IRental {

    public Truck(String plateNumber, String make, String model, LocalDate registeredDate){
        super (plateNumber, make, model, registeredDate, Enums.VehicleCategories.TRUCK.getCode());
    }

    @Override
    public double getDailyRate() {

        double DailyPrice = 550;//Basic price
        Enums.VehicleBrands vehicleBrand = Enums.VehicleBrands
                .getVehicleCategoriesByDescription(getMake().toLowerCase());
        vehicleBrand = vehicleBrand != null ? vehicleBrand : Enums.VehicleBrands.NONE;

        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case Enums.VehicleBrands.TOYOTA:
                DailyPrice = 300;
            case Enums.VehicleBrands.FORD:
                DailyPrice = 400;
            case Enums.VehicleBrands.NISSAN:
                DailyPrice = 225;
        }

        //decrease based of the machine year (older => less expensive)
        int carYear = LocalDate.now().getYear() - getRegisteredDate().getYear();
        DailyPrice -= DailyPrice / carYear; // -= decrease the by year value into daily rate.

        return DailyPrice;
    }

    private transient boolean noPromo = false;
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
                promoPrice -= (promoPrice * .3); // cheaper
                break;
            case Enums.VehicleBrands.NISSAN:
                promoPrice -= (promoPrice * .2); // cheap
                break;

            default:
                noPromo = true;
                break;
        }

        return promoPrice;
    }

    public boolean getNoPromo(){
        return noPromo;
    }
}
