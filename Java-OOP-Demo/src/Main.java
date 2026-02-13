import Adapter.LocalDateAdapter;
import Common.Commons;
import Common.Enums;
import Dtos.Car;
import Dtos.Truck;
import Dtos.Vehicles;
import Interfaces.IVehicleManipulationByMap;
import Services.VehicleManipulationByMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    //Global variable to store all vehicles
    private static final List<Vehicles> allVehicles = new ArrayList<>();

    //After adding a uniqueIdentifier, have the Set for plate number.
    private static Set<Vehicles> existingVehicles = new HashSet<>();

    //Raw JSON Car items
    private final static String jsonCarList =
            """
            [
                {"plateNumber":"QB90JA20", "make":"Toyota", "model":"Camry", "registeredDate":"01-01-2020","category":"1"},
                {"plateNumber":"VA32JA18","make":"Ford", "model":"F-150", "registeredDate":"05-03-2018", "category":"1"}
            ]
            """;

    //Raw JSON Trucks items
    private final static String jsonTruckList =
            """
            [
                {"plateNumber":"PL13MA98","make":"Toyota", "model":"Hilux", "registeredDate":"01-03-1998","category":"2"},
                {"plateNumber":"RH32MY15","make":"Nissan", "model":"Triton", "registeredDate":"15-05-2015","category":"2"}
            ]
            """;


    private static IVehicleManipulationByMap vehicleManipulationByMapService;

    public static void main(String[] args){
        //Make use of services to demonstrate manipulation by add/update/remove and lookup using map.
        //Load up service with vehicles
        vehicleManipulationByMapService = new VehicleManipulationByMap(jsonCarList, jsonTruckList);

        //Display all vehicles
        Scanner consoleReaderForService = new Scanner(System.in);
        System.out.println("\n\nUsing Map: \nAvailable vehicles: ");
        vehicleManipulationByMapService.ShowAllVehicles();

        //Vehicle manipulation via vehicleManipulationByMapService
        //Add
        AddVehicle(consoleReaderForService);
        //Update
        UpdateVehicle(consoleReaderForService);
        //Remove
        RemoveVehicle(consoleReaderForService);

        //Close scanner
        consoleReaderForService.close();
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
            }

            while (category == null) {
                System.out.println("Do you wish to add a car or a truck: ");
                String vehicleType = consoleReaderForService.nextLine();
                if (vehicleType.toLowerCase().equals(Enums.VehicleCategories.CAR.getDescription()))
                {
                    VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                    vehicleManipulationByMapService.AddVehicleByPlate(plateNumber, new Car(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate));
                    category = vehicleType;
                }
                else if(vehicleType.toLowerCase().equals(Enums.VehicleCategories.TRUCK.getDescription()))
                {
                    VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
                    vehicleManipulationByMapService.AddVehicleByPlate(plateNumber, new Truck(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate));
                    category = vehicleType;
                }
                else //error and loop the prior question
                    System.out.println("Invalid vehicle is being added!!");
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

            while(existingVehicle == null){
                System.out.println("Enter plate number: ");
                plateNumber = consoleReaderForService.nextLine();
                existingVehicle = vehicleManipulationByMapService.GetVehicleByPlate(plateNumber);
            }

            boolean isUpdateValid = false;
            while (!isUpdateValid)
            {
                System.out.println("Do you change the type of the vehicle also?(yes/no) ");
                String userConfirmInput = consoleReaderForService.nextLine();
                if(userConfirmInput.equalsIgnoreCase("yes")){
                    while (category == null) {
                        System.out.println("Do you wish to change the vehicle to a car or a truck?");
                        String vehicleType = consoleReaderForService.nextLine();

                        if (vehicleType.toLowerCase().equals(Enums.VehicleCategories.CAR.getDescription())){
                            category = vehicleType;
                            isUpdateValid = true;
                        }

                        else if(vehicleType.toLowerCase().equals(Enums.VehicleCategories.TRUCK.getDescription())){
                            category = vehicleType;
                            isUpdateValid = true;
                        }

                        else//error and loop the prior question
                            System.out.println("Invalid vehicle!!");

                    }
                }
                else if(userConfirmInput.equalsIgnoreCase("no")) {
                    category = Enums.VehicleCategories.getVehicleCategoriesByCode(existingVehicle.getCategory()).getDescription();
                    break;
                }
                else
                    System.out.println("Please enter either yes or no!!");

            }

            VehicleInput vehicleInput = getVehicleInput(consoleReaderForService);
            if (category.equals(Enums.VehicleCategories.CAR.getDescription()))
                vehicleManipulationByMapService.UpdateVehicleByPlate(plateNumber, new Car(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate));
            else if(category.equals(Enums.VehicleCategories.TRUCK.getDescription()))
                vehicleManipulationByMapService.UpdateVehicleByPlate(plateNumber, new Truck(plateNumber, vehicleInput.make, vehicleInput.model, vehicleInput.registeredDate));
            else //error and loop the prior question
                System.out.println("Invalid vehicle is being updated!!");

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

            System.out.println("Updated vehicles list: ");
            vehicleManipulationByMapService.ShowAllVehicles();
        }
    }
    //endregion

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
                System.out.println("Please enter a valid date");
            }
        }

        return new VehicleInput(make, model, registeredDate);
    }

    private record VehicleInput(String make, String model, LocalDate registeredDate) {
    }

}