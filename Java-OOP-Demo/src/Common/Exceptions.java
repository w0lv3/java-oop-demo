package Common;

public class Exceptions {

    //Custom exception
    public static class VehicleNotFoundException extends RuntimeException{
        public VehicleNotFoundException(String plateNumber){
            super("Vehicle with plate number '" + plateNumber + "' not found.");
        }
    }
}
