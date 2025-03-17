package main.java.de.tum.cit.aet.pse.entity;

public class ProductFactory {
    
    public Product createProduct(ProductType type) {
        switch (type) {
            case COCOA:
                return new CocoaMug();
            case MULLED_WINE:
                return new MulledWine();
            case SWEATER:
                return new Sweater();
            default:
                throw new IllegalArgumentException("Unknown product type: " + type);
        }
    }
}
