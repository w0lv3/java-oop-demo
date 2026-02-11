package Common;

public class Enums {

    public enum VehicleCategories{

        CAR("car"),
        TRUCK("truck");

        public final String description;

        //make use of assigned value here being a string for compare text like [Description("Car")] in C#.
        VehicleCategories(String description){
            this.description = description;
        }

        public String getDescription(){
            return description;
        }
    }

}
