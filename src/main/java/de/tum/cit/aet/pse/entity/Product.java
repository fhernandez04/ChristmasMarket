package main.java.de.tum.cit.aet.pse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    
    protected String name;
    protected String description;
    protected double price;

    @Enumerated(EnumType.STRING)
    protected ProductType type;

    @ManyToOne
    @JoinColumn(name = "vendor_id", unique = false)
    private Vendor vendor;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public ProductType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', description='" + description + "', price=" + price + ", type=" + type + ", vendor=" + (vendor != null ? vendor.getName() : "null") + "}";
    }

    // Special effect if needed
    public abstract void consume(User user);   
}