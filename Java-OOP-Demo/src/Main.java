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
    private static IVehicleManipulationService vehicleManipulationService;

    public static boolean isYes(Scanner consoleReaderForService) {
        String input = consoleReaderForService.nextLine().toLowerCase();
        return input.equals("yes");
    }

    public static void main(String[] args) {
        logger.info("Application Starting");
        //Make use of services to demonstrate manipulation by add/update/remove and lookup using map.
        //Load up service with vehicles
        vehicleManipulationService = new VehicleManipulationService();

        //Display all vehicles
        Scanner consoleReaderForService = new Scanner(System.in);
        System.out.println("\n\nUsing Map: \nAvailable vehicles: ");
        vehicleManipulationService.ShowAllVehicles();

        //CRUD operations
        CRUDOperations(consoleReaderForService);
        //Renting section
        RentOperations(consoleReaderForService);

        //Close scanner
        consoleReaderForService.close();
        logger.info("Application Closing");
    }

    private static void CRUDOperations(Scanner consoleReaderForService) {
        logger.info("CRUD Services");

        //Add
        System.out.println("Do you wish to Add a new vehicle? Enter yes or press any key to continue!");
        if(isYes(consoleReaderForService))
            AddVehicle(consoleReaderForService);

        //Update
        System.out.println("Do you wish to update an existing vehicle? Enter yes or press any key to continue!");
        if(isYes(consoleReaderForService))
            UpdateVehicle(consoleReaderForService);

        //Remove
        System.out.println("Do you wish to remove an existing vehicle? Enter yes or press any key to continue!");
        if(isYes(consoleReaderForService))
            RemoveVehicle(consoleReaderForService);
    }

    //region method for vehicle manipulations
    private static void AddVehicle(Scanner consoleReaderForService) {
        logger.info("ADD Ops");
        String plateNumber = null;
        String category = null;
        boolean vehicleExists = true;
        while (vehicleExists) {
            System.out.println("Enter plate number: ");
            plateNumber = consoleReaderForService.nextLine();
            if(plateNumber != null && !plateNumber.isBlank() && !plateNumber.isEmpty() )
                //lookup by plateNumber using vehicleManipulationByMapService.SearchByPlate
                vehicleExists = vehicleManipulationService.SearchByPlate(plateNumber);

            if (!vehicleExists)
                System.out.print("We can proceed. ");
        }

        while (category == null) {
            System.out.println("Do you wish to add a car, a van, a pickup or an SUV: ");
            String vehicleType = consoleReaderForService.nextLine();
            if (vehicleType.toLowerCase().equals(Enums.VehicleCategories.CAR.getDescription())) {
                VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                vehicleManipulationService.AddVehicleByPlate(plateNumber, new Car(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, true));
                category = vehicleType;
                logger.info("Car added successfully!");
            } else if (vehicleType.toLowerCase().equals(Enums.VehicleCategories.VAN.getDescription())) {
                VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                vehicleManipulationService.AddVehicleByPlate(plateNumber, new Van(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, true));
                category = vehicleType;
                logger.info("Van added successfully!");
            } else if (vehicleType.toLowerCase().equals(Enums.VehicleCategories.PICKUP.getDescription())) {
                VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                vehicleManipulationService.AddVehicleByPlate(plateNumber, new Pickup(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, true));
                category = vehicleType;
                logger.info("Pickup added successfully!");
            } else if (vehicleType.toLowerCase().equals(Enums.VehicleCategories.SUV.getDescription())) {
                VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                vehicleManipulationService.AddVehicleByPlate(plateNumber, new SUV(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, true));
                category = vehicleType;
                logger.info("SUV added successfully!");
            } else //error and loop the prior question
            {
                System.out.println("Please enter a valid type of transport.");
                logger.warn("Invalid vehicle is being added!!");
            }
        }

        System.out.println("Updated vehicles list: ");
        vehicleManipulationService.ShowAllVehicles();

    }

    private static void UpdateVehicle(Scanner consoleReaderForService) {
        logger.info("UPDATE Ops");
        Vehicles existingVehicle = null;
        String plateNumber = null;
        String category = null;
        Enums.VehicleCategories vehicleType = null;

        while (existingVehicle == null) {
            System.out.println("Enter plate number: ");
            plateNumber = consoleReaderForService.nextLine();
            existingVehicle = vehicleManipulationService.GetVehicleByPlate(plateNumber);
            category = Enums.VehicleCategories.getByCode(existingVehicle.getCategory()).getDescription();
            vehicleType = Enums.VehicleCategories.getByDescription(category);
        }

        boolean isUpdateValid = false;
        while (!isUpdateValid) {
            System.out.println("Do you change the type of the vehicle also?(yes/no) ");
            String userConfirmInput = consoleReaderForService.nextLine();
            if (userConfirmInput.equalsIgnoreCase("yes")) {
                vehicleType = null;
                while (vehicleType == null) {
                    System.out.println("Do you wish to change the vehicle to a car, a van, a pickup or an SUV?");
                    String userVehicleType = consoleReaderForService.nextLine();
                    vehicleType = Enums.VehicleCategories.getByDescription(userVehicleType.toLowerCase());

                    if (vehicleType != null) {
                        isUpdateValid = true;
                    } else//error and loop the prior question
                    {
                        System.out.println("Please enter a valid transport type.");
                        logger.warn("Invalid vehicle!!");
                    }
                }
            } else if (userConfirmInput.equalsIgnoreCase("no")) {
                break;
            } else
                System.out.println("Please enter either yes or no!!");

        }

        VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
        switch (vehicleType) {
            case CAR:
                vehicleManipulationService.UpdateVehicleByPlate(plateNumber, new Car(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, existingVehicle.getAvailability()));
                break;
            case VAN:
                vehicleManipulationService.UpdateVehicleByPlate(plateNumber, new Van(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, existingVehicle.getAvailability()));
                break;
            case PICKUP:
                vehicleManipulationService.UpdateVehicleByPlate(plateNumber, new Pickup(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, existingVehicle.getAvailability()));
                break;
            case SUV:
                vehicleManipulationService.UpdateVehicleByPlate(plateNumber, new SUV(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate, existingVehicle.getAvailability()));
                break;
        }

        if (isUpdateValid)
            logger.info("Vehicle updated successfully!");

        System.out.println("Updated vehicles list: ");
        vehicleManipulationService.ShowAllVehicles();

    }

    private static void RemoveVehicle(Scanner consoleReaderForService) {
        logger.info("DELETE Ops");
        String plateNumber = null;
        boolean vehicleExists = false;

        while (!vehicleExists) {
            System.out.println("Enter plate number: ");
            plateNumber = consoleReaderForService.nextLine();
            //lookup by plateNumber using vehicleManipulationByMapService.SearchByPlate
            vehicleExists = vehicleManipulationService.SearchByPlate(plateNumber);
        }
        vehicleManipulationService.DeleteVehicleByPlate(plateNumber);
        logger.info("Vehicle removed successfully!");

        System.out.println("Updated vehicles list: ");
        vehicleManipulationService.ShowAllVehicles();

    }
    //endregion

    private static void RentOperations(Scanner consoleReaderForService) {
        boolean exit = false;
        System.out.println("Do you wish to rent a vehicle! Enter yes or press any key to continue!");
        if(isYes(consoleReaderForService)){
            while(!exit)
            {
                System.out.println("Rental Services");
                rentalOperations(consoleReaderForService);

                System.out.println("Do you wish to rent another vehicle! Enter yes or press any key to continue!");
                if(isYes(consoleReaderForService))
                    rentalOperations(consoleReaderForService);
                else
                    exit = true;
            }
        }
    }

    private static void rentalOperations(Scanner consoleReaderForService) {
        logger.info("Rental Services");
        IRentalService rentalService = new RentalService();// init
        Vehicles existingVehicle = null;
        String plateNumber = null;

        while(existingVehicle == null){
            System.out.println("Enter plate number: ");
            plateNumber = consoleReaderForService.nextLine();
            existingVehicle = vehicleManipulationService.GetVehicleByPlate(plateNumber);
        }
        double totalCost = -1;
        while(totalCost < 0){
            System.out.println("Enter the number of rental days: ");
            String inputDays = consoleReaderForService.nextLine();
            int days = Integer.parseInt(inputDays);
            if(days > 0){
                totalCost = rentalService.GetSelectedTotalCost(existingVehicle, days);
                System.out.println("Your total cost will be $"+ totalCost);
            }
            else
                System.out.println("Please enter a valid set of days");
        }

        System.out.println("Do you wish to continue with renting the selected vehicle? Enter yes or press any key to cancel!");
        if(isYes(consoleReaderForService)){
            existingVehicle.SetAvailability(false);
            vehicleManipulationService.UpdateVehicleByPlate(existingVehicle.getPlateNumber(), existingVehicle);
            System.out.println(existingVehicle + " Rented out!");
            rentalService.DisplayAvailableVehicles();
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
                String inputDate = consoleReader.nextLine();
                registeredDate = LocalDate.parse(inputDate, Commons.dateFormatter);
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