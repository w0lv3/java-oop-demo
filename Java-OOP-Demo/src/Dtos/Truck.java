package Dtos;

import Common.Enums;
import Interfaces.IRental;

import java.time.LocalDate;

public class Truck extends Vehicles implements IRental {

    public Truck(String plateNumber, String make, String model, LocalDate registeredDate){
        super (plateNumber, make, model, registeredDate, Enums.VehicleCategories.TRUCK.getCode());
    }

    private boolean noPromo = false;
    //Insurance section
    @Override
    public double getRentalPrice() {
        double rentalBasePrice = 500;

        //Increment based of the machine year (older => more expensive)
        int carYear = LocalDate.now().getYear() - getRegisteredDate().getYear();
        rentalBasePrice += carYear * 20; // += increment the by year value into base

        switch (getMake().toLowerCase()) { //set to lowercase to avoid Case typo issues
            case "toyota":
                rentalBasePrice -= (rentalBasePrice * .5); //cheapest
                break;
            case "ford":
                rentalBasePrice -= (rentalBasePrice * .3); // cheaper
                break;
            case "nissan":
                rentalBasePrice -= (rentalBasePrice * .2); // cheap
                break;

            default:
                noPromo = true;
                break;
        }

        return rentalBasePrice;
    }

    public boolean getNoPromo(){
        return noPromo;
    }
}
