package main.java.de.tum.cit.aet.pse.entity;

import jakarta.persistence.Entity;

@Entity
public class MulledWine extends Product {

    public MulledWine() {
        this.name = "Mulled Wine";
        this.description = "A cup of mulled wine.";
        this.price = 5.0;
        this.type = ProductType.MULLED_WINE;
    }

    @Override
    public void consume(User user) {
        if (user.getDrunkness() == 5) {
            System.out.println("You are already drunk. You can't continue drinking before some cocoamug, I will take it for you :)");
            return;
        }

        System.out.println("You drank a cup of mulled wine.");
        user.increaseDrunkness();
        System.out.println("Your drunkness level is now " + user.getDrunkness() + ".");

        if (user.getDrunkness() == 5) {
            System.out.println("You are drunk.");
        }
    }
}