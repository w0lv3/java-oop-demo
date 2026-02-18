package Interfaces;

public interface IRental {

    // Polymorphic behavior by using the getRentalPrice from its extensions
    double getPromoPrice();
    boolean getNoPromo();
    double getDailyRate();
}
