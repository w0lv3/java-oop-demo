package Services;

import Common.Enums;
import Common.Exceptions;
import Dtos.Vehicles;
import Interfaces.IFileManipulationService;
import Interfaces.IRentalService;

import java.time.LocalDate;
import java.util.stream.Stream;

public class RentalService implements IRentalService {

    IFileManipulationService fileManipulationService = new FileManipulationService();

    public RentalService(){
        //InIt with only available for rental vehicles
        DisplayAvailableVehicles();
    }

    public void DisplayAvailableVehicles(){
        System.out.println("\nAvailable vehicles for rent: ");
        Stream<Vehicles> availableVehiclesMap = fileManipulationService.GetAvailableVehiclesFromFile();
        availableVehiclesMap.forEach(v-> System.out.println(v.rentalDisplay()
                + " | " + "Daily rental price: $" + String.format("%.2f", getPromoPrice(v))
                + (v.getNoPromo() ? " Promotional price not applied!" : "") ));

        System.out.println("\nRented out vehicles: ");
        Stream<Vehicles> rentedVehiclesMap = fileManipulationService.GetRentedVehiclesFromFile();
        rentedVehiclesMap.forEach(v-> System.out.println(v.rentalDisplay()
                + " | " + "Daily rental price: $" + String.format("%.2f", getPromoPrice(v))
                + (v.getNoPromo() ? " Promotional price not applied!" : "") ));
    }

    public double GetSelectedTotalCost(Vehicles selectedVehicle, int days){
        return getPromoPrice(selectedVehicle) * days;
    }

    public double getPromoPrice(Vehicles selectedVehicle) {
        double promoPrice = getDailyRate(selectedVehicle);
        Enums.VehicleBrands vehicleBrand = Enums.VehicleBrands
                .getByDescription(selectedVehicle.getMake().toLowerCase());

        vehicleBrand = vehicleBrand != null ? vehicleBrand : Enums.VehicleBrands.NONE;
        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case TOYOTA:
                promoPrice -= (promoPrice * .15); //cheaper
                break;
            case FORD:
                promoPrice -= (promoPrice * .10); //cheap
                break;
            case NISSAN:
                promoPrice -= (promoPrice * .20); //cheapest
                break;
            default:
                selectedVehicle.setNoPromo(true);
                break;
        }

        return promoPrice;
    }

    private double getDailyRate(Vehicles selectedVehicle) {

        Enums.VehicleBrands vehicleBrand = Enums.VehicleBrands
                .getByDescription(selectedVehicle.getMake().toLowerCase());
        vehicleBrand = vehicleBrand != null ? vehicleBrand : Enums.VehicleBrands.NONE;

        double dailyPrice = -1;
        Enums.VehicleCategories selectedVehicleType = Enums.VehicleCategories.getByCode(selectedVehicle.getCategory());
        switch (selectedVehicleType){
            case CAR:
                dailyPrice = GetCarDailyPrice(vehicleBrand);
                break;
            case VAN:
                dailyPrice = GetVanDailyPrice(vehicleBrand);
                break;
            case PICKUP:
                dailyPrice = GetPickupDailyPrice(vehicleBrand);
                break;
            case SUV:
                dailyPrice = GetSUVDailyPrice(vehicleBrand);
                break;
        }
        if(dailyPrice < 0)
            throw new Exceptions.InvalidDailyPriceException();

        //decrease based of the machine year (older => less expensive)
        //make use of Math.max to avoid divide by 0 for an entry existing in the same year of now.
        int carYear = Math.max(1,(LocalDate.now().getYear() - selectedVehicle.getRegisteredDate().getYear()));
        if(carYear > 10)
            dailyPrice -= dailyPrice * 0.15; //-= decrease the by year value into daily rate.
        if(carYear > 20)
            dailyPrice -= dailyPrice * 0.25;


        return dailyPrice;
    }

    private static double GetCarDailyPrice(Enums.VehicleBrands vehicleBrand) {
        double dailyPrice = 450;//Basic price
        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case TOYOTA:
                dailyPrice = 225;
                break;
            case FORD:
                dailyPrice = 300;
                break;
            case NISSAN:
                dailyPrice = 125;
                break;
        }
        return dailyPrice;
    }

    private static double GetVanDailyPrice(Enums.VehicleBrands vehicleBrand) {
        double dailyPrice = 300;//Basic price
        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case TOYOTA:
                dailyPrice = 250;
                break;
            case RENAULT:
                dailyPrice = 325;
                break;
            case NISSAN:
                dailyPrice = 225;
                break;
        }
        return dailyPrice;
    }

    private static double GetPickupDailyPrice(Enums.VehicleBrands vehicleBrand) {
        double dailyPrice = 550;//Basic price
        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case TOYOTA:
                dailyPrice = 300;
                break;
            case FORD:
                dailyPrice = 400;
                break;
            case NISSAN:
                dailyPrice = 225;
                break;
        }
        return dailyPrice;
    }

    private static double GetSUVDailyPrice(Enums.VehicleBrands vehicleBrand) {
        double dailyPrice = 600;//Basic price
        switch (vehicleBrand) { //set to lowercase to avoid Case typo issues
            case TOYOTA:
                dailyPrice = 350;
                break;
            case FORD:
                dailyPrice = 400;
                break;
            case NISSAN:
                dailyPrice = 300;
                break;
        }
        return dailyPrice;
    }
}
