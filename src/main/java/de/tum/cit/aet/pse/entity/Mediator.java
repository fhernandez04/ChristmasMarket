package main.java.de.tum.cit.aet.pse.entity;

public interface Mediator {
    String addToCart(int userId, ProductType productType, int vendorId, int quantity);
    String removeFromCart(int userId, ProductType productType, int quantity);
    String purchase(int userId);
}
