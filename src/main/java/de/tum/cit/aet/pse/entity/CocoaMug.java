package main.java.de.tum.cit.aet.pse.entity;
import jakarta.persistence.Entity;

@Entity
public class CocoaMug extends Product {

    public CocoaMug() {
        this.name = "Cocoa Mug";
        this.description = "A cup of cocoa.";
        this.price = 3.0;
        this.type = ProductType.COCOA;
    }

    @Override
    public void consume(User user) {
        user.decreaseDrunkness();
        System.out.println("You drank a cup of cocoa. Your drunkness level is now " + user.getDrunkness() + ".");
    }
    
}
