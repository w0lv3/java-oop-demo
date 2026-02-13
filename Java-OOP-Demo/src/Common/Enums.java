package Common;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Enums {

    public enum VehicleCategories{

        CAR(1,"car"),
        TRUCK(2,"truck");

        private final String description;
        private final int code;
        private static final LinkedHashMap <Integer, VehicleCategories> vehicleCategoriesByCode = new LinkedHashMap<>();

        //Initialize the Enum mapper.
        static {
            for (VehicleCategories categories : values()){
                vehicleCategoriesByCode.put(categories.code, categories);
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

        public static VehicleCategories getVehicleCategoriesByCode(Integer code) {
            return vehicleCategoriesByCode.get(code);
        }
    }

}
