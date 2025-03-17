package main.java.de.tum.cit.aet.pse.entity;
import jakarta.persistence.Entity;

@Entity
public class Sweater extends Product {

    public Sweater() {
        this.name = "Christmas Sweater";
        this.description = "A hand-knit Christmas sweater.";
        this.price = 25.0;
        this.type = ProductType.SWEATER;
    }

    @Override
    public void consume(User user) {
        System.out.println("You are wearing a Christmas sweater.");
    }
    
}
