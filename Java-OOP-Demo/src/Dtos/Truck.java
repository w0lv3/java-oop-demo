package Dtos;

import Interfaces.IRental;

import java.time.LocalDate;

public class Truck extends Vehicles implements IRental {

    public Truck(String plateNumber, String make, String model, LocalDate registeredDate){
        super (plateNumber, make, model, registeredDate);
    }

    @Override
    public String getCategory() {
        return "Truck";
    }

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
                System.out.println("Promotional price not applied!");
                break;
        }

        return rentalBasePrice;
    }
}
