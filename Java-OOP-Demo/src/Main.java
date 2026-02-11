import Adapter.LocalDateAdapter;
import Common.Commons;
import Common.Enums;
import Dtos.Car;
import Dtos.Truck;
import Dtos.Vehicles;
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
    private static final List<Vehicles> allVehicles = new ArrayList<Vehicles>();

    //After adding a uniqueIdentifier, have the Set for plate number.
    private static Set<String> existingVehicles = new HashSet<>();

    //Raw JSON Car items
    private static String jsonCarList =
            """
            [
                {"plateNumber":"QB90JA20", "make":"Toyota", "model":"Camry", "registeredDate":"01-01-2020"},
                {"plateNumber":"VA32JA18","make":"Ford", "model":"F-150", "registeredDate":"05-03-2018"}
            ]
            """;

    //Raw JSON Trucks items
    private static String jsonTruckList =
            """
            [
                {"plateNumber":"PL13MA98","make":"Toyota", "model":"Hilux", "registeredDate":"01-03-1998"},
                {"plateNumber":"RH32MY15","make":"Nissan", "model":"Triton", "registeredDate":"15-05-2015"}
            ]
            """;

    public static void main(String[] args) throws InterruptedException {

        //Stack vs heap
        String stackVariable = "vehicle";
        Car heapVariable = new Car("1010JA90","Toyota", "Rav4", LocalDate.of(1990,1,31));
        System.out.printf("\nThis a stack : %s",stackVariable );
        System.out.printf("\nThis a heap : %s\n",heapVariable.toString());

        //Fill car list.
        AddListOfCars();
        AddListOfTrucks();
        //Fill Set of plate number
        existingVehicles = allVehicles.stream().map(v->v.getPlateNumber()).collect(Collectors.toSet());

        //Display all vehicles
        DisplayAllVehicles();

        System.out.println("\nThe rental company have these cars: ");
        //Display all Cars only
        allVehicles.stream()
                .filter(v-> v.getCategory() == "Car")
                .forEach(v-> System.out.println(v));

        System.out.println("\nand these trucks: ");
        //Display all Trucks only
        allVehicles.stream()
                .filter(v-> v.getCategory() == "Truck")
                .forEach(v-> System.out.println(v));

        //Display promo vehicles
        System.out.println("\nWe recommend the Toyota vehicles as there is a current promotion. The Toyota vehicles are: ");
        allVehicles.stream()
                .filter(v-> v.getMake().equals("Toyota"))
                .peek(v-> System.out.println(v.getCategory() + " " + v));

        // create a Map collection of vehicles grouped by the make
        System.out.println("Available vehicles by brand:");
        System.out.printf("\nWe have a total of %d vehicles.", allVehicles.size()); //.size() is like .Count() in C#
        Map<String, List<Vehicles>> vehiclesByMake = allVehicles.stream().collect(Collectors.groupingBy(v -> v.getMake()));
        vehiclesByMake.forEach((make, vehicles)-> System.out.printf("\n%d from the %s brand.", vehicles.size(), make));

        //Have an add using existingVehicles set to verify if added vehicle exist within the rental.
        UserAddVehicle();
        DisplayAllVehicles();

        //Create a lookup by plateNumber using ...

        //After adding a status prop show available vehicles.
    }

    private static <T> List<T> parseJsonList(String json, Type type) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())//make use of the custom adapter to handle LocalDate deserialization
                .create();
        return gson.fromJson(json, type);
    }

    private static void AddListOfCars(){

        //Simulate the imported cars from a JSON string.
        Type jsonType = new TypeToken<List<Car>>(){}.getType();
        List<Car> importedCars = parseJsonList(jsonCarList, jsonType);

        for(Car car:importedCars)
        {
            AddCar(car.getPlateNumber(), car.getMake(), car.getModel(), car.getRegisteredDate());
        }
    }

    private static void AddListOfTrucks() {
        //Simulate the imported Trucks from a JSON string.
        Type jsonType = new TypeToken<List<Truck>>(){}.getType();
        List<Truck> importedTrucks = parseJsonList(jsonTruckList, jsonType);

        for(Truck truck:importedTrucks)
        {
            AddTruck(truck.getPlateNumber(), truck.getMake(), truck.getModel(), truck.getRegisteredDate());
        }

    }

    private static void AddCar(String plateNumber, String make, String model, LocalDate registeredDate) {
        allVehicles.add(new Car(plateNumber,make,model,registeredDate));
    }

    private static void AddTruck(String plateNumber, String make, String model, LocalDate registeredDate) {
        allVehicles.add(new Truck(plateNumber, make,model,registeredDate));
    }

    private static void DisplayAllVehicles() {
        System.out.println("\nAll vehicles that the rental company owns are: ");
        allVehicles.forEach(v-> System.out.println(v));
    }

    private static void UserAddVehicle() {
        Scanner consoleReader = new Scanner(System.in);
        System.out.println("\nDo you wish to add an additional vehicle?");
        boolean approve = consoleReader.nextLine().toLowerCase().equals("yes") ? true : false;

        if(approve)
        {
            String plate = null;
            while (plate == null)
            {
                System.out.println("Enter plate number: ");
                String input = consoleReader.nextLine();
                if (!existingVehicles.contains(input))
                    plate = input;
                else
                    System.out.println("Entered plate number already exists.");

            }

            System.out.println("Do you wish to add a car or a truck: ");
            String category = consoleReader.nextLine();
            if (category.toLowerCase().equals(Enums.VehicleCategories.CAR.description)) {

                VehicleInput input = getVehicleInput(consoleReader);
                AddCar(plate, input.make(), input.model(), input.registeredDate());
            }

            if(category.toLowerCase().equals(Enums.VehicleCategories.TRUCK.description)) {

                VehicleInput input = getVehicleInput(consoleReader);
                AddTruck(plate, input.make(), input.model(), input.registeredDate());
            }
            else//error and exit
                return;

            //close scanner
            consoleReader.close();
        }

    }

    //Get vehicle input
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