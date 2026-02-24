import Common.Commons;
import Common.Enums;
import Dtos.*;
import Interfaces.IRentalService;
import Interfaces.IVehicleManipulationService;
import Services.RentalService;
import Services.VehicleManipulationService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    //Global variable to store all vehicles
    private static final List<Vehicles> allVehicles = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static IVehicleManipulationService vehicleManipulationByMapService;
    private static IRentalService rentalService;

    public static void main(String[] args){
        logger.info("Application Starting");
        //Make use of services to demonstrate manipulation by add/update/remove and lookup using map.
        //Load up service with vehicles
        vehicleManipulationByMapService = new VehicleManipulationService();

        //Display all vehicles
        Scanner consoleReaderForService = new Scanner(System.in);
        System.out.println("\n\nUsing Map: \nAvailable vehicles: ");
        vehicleManipulationByMapService.ShowAllVehicles();

        //CRUD operations
        //Add
        AddVehicle(consoleReaderForService);
        //Update
        UpdateVehicle(consoleReaderForService);
        //Remove
        RemoveVehicle(consoleReaderForService);

        //Renting section
        rentalOperations(consoleReaderForService);

        //Close scanner
        consoleReaderForService.close();
        logger.info("Application Closing");
    }

    //region method for vehicle manipulations
    private static void AddVehicle(Scanner consoleReaderForService) {
        String plateNumber = null;
        String category = null;

        System.out.println("Do you wish to Add a new vehicle? Enter yes or press any key to continue!");
        boolean toAdd = consoleReaderForService.nextLine().equalsIgnoreCase("yes");

        if(toAdd){
            boolean vehicleExists = true;
            while(vehicleExists) {
                System.out.println("Enter plate number: ");
                plateNumber = consoleReaderForService.nextLine();
                //lookup by plateNumber using vehicleManipulationByMapService.SearchByPlate
                vehicleExists = vehicleManipulationByMapService.SearchByPlate(plateNumber);
                if(!vehicleExists)
                    System.out.print("We can proceed. ");
            }

            while (category == null) {
                System.out.println("Do you wish to add a car, a van, a pickup or an SUV: ");
                String vehicleType = consoleReaderForService.nextLine();
                if (vehicleType.toLowerCase().equals(Enums.VehicleCategories.CAR.getDescription()))
                {
                    VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                    vehicleManipulationByMapService.AddVehicleByPlate(plateNumber, new Car(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, true));
                    category = vehicleType;
                    logger.info("Car added successfully!");
                }
                else if(vehicleType.toLowerCase().equals(Enums.VehicleCategories.VAN.getDescription()))
                {
                    VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                    vehicleManipulationByMapService.AddVehicleByPlate(plateNumber, new Van(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, true));
                    category = vehicleType;
                    logger.info("Van added successfully!");
                }
                else if(vehicleType.toLowerCase().equals(Enums.VehicleCategories.PICKUP.getDescription()))
                {
                    VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                    vehicleManipulationByMapService.AddVehicleByPlate(plateNumber, new Pickup(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, true));
                    category = vehicleType;
                    logger.info("Pickup added successfully!");
                }
                else if(vehicleType.toLowerCase().equals(Enums.VehicleCategories.SUV.getDescription()))
                {
                    VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                    vehicleManipulationByMapService.AddVehicleByPlate(plateNumber, new SUV(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, true));
                    category = vehicleType;
                    logger.info("SUV added successfully!");
                }
                else //error and loop the prior question
                {
                    System.out.println("Please enter a valid type of transport.");
                    logger.warn("Invalid vehicle is being added!!");
                }
            }

            System.out.println("Updated vehicles list: ");
            vehicleManipulationByMapService.ShowAllVehicles();
        }
    }

    private static void UpdateVehicle(Scanner consoleReaderForService) {
        System.out.println("Do you wish to update an existing vehicle? Enter yes or press any key to continue!");
        boolean toUpdate = consoleReaderForService.nextLine().equalsIgnoreCase("yes");

        if(toUpdate){
            Vehicles existingVehicle = null;
            String plateNumber = null;
            String category = null;
            Enums.VehicleCategories vehicleType = null;

            while(existingVehicle == null){
                System.out.println("Enter plate number: ");
                plateNumber = consoleReaderForService.nextLine();
                existingVehicle = vehicleManipulationByMapService.GetVehicleByPlate(plateNumber);
                category = Enums.VehicleCategories.getByCode(existingVehicle.getCategory()).getDescription();
                vehicleType = Enums.VehicleCategories.getByDescription(category);
            }

            boolean isUpdateValid = false;
            while (!isUpdateValid)
            {
                System.out.println("Do you change the type of the vehicle also?(yes/no) ");
                String userConfirmInput = consoleReaderForService.nextLine();
                if(userConfirmInput.equalsIgnoreCase("yes")){
                    vehicleType = null;
                    while (vehicleType == null) {
                        System.out.println("Do you wish to change the vehicle to a car, a van, a pickup or an SUV?");
                        String userVehicleType = consoleReaderForService.nextLine();
                        vehicleType = Enums.VehicleCategories.getByDescription(userVehicleType.toLowerCase());

                        if(vehicleType != null) {
                            isUpdateValid = true;
                        }
                        else//error and loop the prior question
                        {
                            System.out.println("Please enter a valid transport type.");
                            logger.warn("Invalid vehicle!!");
                        }
                    }
                }
                else if(userConfirmInput.equalsIgnoreCase("no")) {
                    break;
                }
                else
                    System.out.println("Please enter either yes or no!!");

            }

            VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
            switch (vehicleType){
                case CAR:
                    vehicleManipulationByMapService.UpdateVehicleByPlate(plateNumber, new Car(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, existingVehicle.getAvailability()));
                    break;
                case VAN:
                    vehicleManipulationByMapService.UpdateVehicleByPlate(plateNumber, new Van(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, existingVehicle.getAvailability()));
                    break;
                case PICKUP:
                    vehicleManipulationByMapService.UpdateVehicleByPlate(plateNumber, new Pickup(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, existingVehicle.getAvailability()));
                    break;
                case SUV:
                    vehicleManipulationByMapService.UpdateVehicleByPlate(plateNumber, new SUV(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, existingVehicle.getAvailability()));
                    break;
            }

            if(isUpdateValid)
                logger.info("Vehicle updated successfully!");

            System.out.println("Updated vehicles list: ");
            vehicleManipulationByMapService.ShowAllVehicles();
        }
    }

    private static void RemoveVehicle(Scanner consoleReaderForService) {
        String plateNumber = null;

        System.out.println("Do you wish to remove an existing vehicle? Enter yes or press any key to continue! ");
        boolean toRemove = consoleReaderForService.nextLine().equalsIgnoreCase("yes");

        if(toRemove){
            boolean vehicleExists = false;
            while(!vehicleExists) {
                System.out.println("Enter plate number: ");
                plateNumber = consoleReaderForService.nextLine();
                //lookup by plateNumber using vehicleManipulationByMapService.SearchByPlate
                vehicleExists = vehicleManipulationByMapService.SearchByPlate(plateNumber);
            }

            vehicleManipulationByMapService.DeleteVehicleByPlate(plateNumber);
            logger.info("Vehicle removed successfully!");

            System.out.println("Updated vehicles list: ");
            vehicleManipulationByMapService.ShowAllVehicles();
        }
    }
    //endregion

    private static void rentalOperations(Scanner consoleReaderForService) {
        rentalService = new RentalService();// init
        Vehicles existingVehicle = null;
        String plateNumber = null;

        while(existingVehicle == null){
            System.out.println("Enter plate number: ");
            plateNumber = consoleReaderForService.nextLine();
            existingVehicle = vehicleManipulationByMapService.GetVehicleByPlate(plateNumber);
        }
        double totalCost = -1;
        while(totalCost < 0){
            System.out.println("Enter the number of rental days: ");
            int days = consoleReaderForService.nextInt();
            if(days >= 0){
                totalCost = rentalService.GetSelectedTotalCost(existingVehicle, days);
                System.out.println("Your total cost will be $"+ totalCost);
            }
            else
                System.out.println("Please enter a valid set of days");
        }

        System.out.println("Do you wish to continue with renting the selected vehicle? Enter yes or press any key to continue!");
        boolean toRent = consoleReaderForService.nextLine().equalsIgnoreCase("yes");

        if(toRent){
            //TODO to update the selected vehicle status
            ;
        }else {
            //TODO to rerun the rental func
            ;
        }


    }

    //Get user input
    private static VehicleInput getVehicleInput(Scanner consoleReader) {
        System.out.println("Please enter make: ");
        String make = consoleReader.nextLine();
        System.out.println("Please enter model: ");
        String model = consoleReader.nextLine();

        LocalDate registeredDate = null;
        while (registeredDate == null)
        {
            System.out.println("Please enter registeredDate(format dd-mm-yyyy): ");

            try{
                registeredDate = LocalDate.parse(consoleReader.nextLine(), Commons.dateFormatter);
            }

            catch(DateTimeParseException e) {
                logger.error("Please enter a valid date");
            }
        }

        return new VehicleInput(make, model, registeredDate);
    }

    private record VehicleInput(String make, String model, LocalDate registeredDate) {
    }

}