package Common;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Enums {


    public enum VehicleCategories{

        CAR(1,"Car"),
        VAN(2,"Van"),
        PICKUP(3,"Pickup"),
        SUV(4,"SUV");


        private final String description;
        private final int code;
        private static final LinkedHashMap <Integer, VehicleCategories> vehicleCategoriesByCode = new LinkedHashMap<>();

        //Initialize the Enum mapper.
        static {
            for (VehicleCategories categories : values()){
                vehicleCategoriesByCode.put(categories.code, categories);
            }
        }

        private static final LinkedHashMap <String, VehicleCategories> vehicleCategoriesByDescription = new LinkedHashMap<>();

        //Initialize the Enum mapper.
        static {
            for (VehicleCategories categories : values()){
                vehicleCategoriesByDescription.put(categories.description.toLowerCase(), categories);
            }
        }

        //make use of assigned value here being a string for compare text like [Description("Car")] in C#.
        VehicleCategories(int code, String description){
            this.code = code;
            this.description = description;
        }

        public int getCode(){
            return code;
        }
        public String getDescription(){
            return description;
        }

        public static VehicleCategories getByCode(Integer code) {
            return vehicleCategoriesByCode.get(code);
        }

        public static VehicleCategories getByDescription(String description) {
            return vehicleCategoriesByDescription.get(description);
        }
    }

    public enum VehicleBrands{
        NONE(""),
        FORD("Ford"),
        TOYOTA("Toyota"),
        NISSAN("Nissan"),
        RENAULT("Renault");

        private final String description;
        private static final HashMap <String, VehicleBrands> VehicleBrandsByDescription = new HashMap<>();

        //Initialize the Enum mapper.
        static {
            for (VehicleBrands made : values()){
                VehicleBrandsByDescription.put(made.description.toLowerCase(), made);
            }
        }

        //make use of assigned value here being a string for compare text like [Description("Car")] in C#.
        VehicleBrands(String description){this.description = description;}

        public String getDescription(){
            return description;
        }

        public static VehicleBrands getByDescription(String description) {
            return VehicleBrandsByDescription.get(description);
        }

    }

}
