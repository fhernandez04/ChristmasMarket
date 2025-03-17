package main.java.de.tum.cit.aet.pse.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    private double money;

    @OneToMany
    private List<Product> products = new ArrayList<>();

    // describes the position of the last product of a certain type in the products list
    // Graphic example: 
    // [COCOA, COCOA, COCOA, COCOA, SWEATER, SWEATER, SWEATER]
    // stock = {COCOA: 4, SWEATER: 7}
    @ElementCollection
    private Map<ProductType, Integer> indexStock = new HashMap<>();

    @OneToMany
    private List<Product> sales = new ArrayList<>();

    // describes the position of the last product of a certain type in the sales list
    // Graphic example: 
    // [COCOA, COCOA, COCOA, COCOA, SWEATER, SWEATER, SWEATER]
    // stock = {COCOA: 4, SWEATER: 7}
    @ElementCollection
    private Map<ProductType, Integer> indexSales = new HashMap<>();

    public Vendor() {
    }

    public Vendor(String name, String address, List<Product> products, List<Product> sales) {
        this.name = name;
        this.address = address;
        money = 0;
        
        // Sort products and configure stock map
        for (Product product : products) {
            product.setVendor(this);
            incrementProductStock(product.getType(), product);
        }
        // Sort sales and configure sales map
        for (Product product : sales) {
            addSale(product);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products.clear();
        this.indexStock.clear();
        for (Product product : products) {
            incrementProductStock(product.getType(), product);
        }
    }

    public List<Product> getSales() {
        return sales;
    }

    public void setSales(List<Product> sales) {
        this.sales.clear();
        this.indexSales.clear();
        for (Product product : sales) {
            addSale(product);
        }
    }

    public Map<ProductType, Integer> getIndexStock() {
        return indexStock;
    }

    public void setIndexStock(Map<ProductType, Integer> stock) {
        this.indexStock = stock;
    }

    public void increaseMoney(double amount) {
        money += amount;
    } 

    public double getMoney() {
        return money;
    }

    // increments the stock of a certain product type by a certain product
    public void incrementProductStock(ProductType productType, Product product) {
        if (product.getType() != productType) {
            throw new IllegalArgumentException("Mismatch between product type and expected type: " + productType);
        }

        int position = indexStock.getOrDefault(productType, products.size());
        products.add(position, product);
        indexStock.put(productType, position + 1);

        // Update index for remaining products
        for (ProductType type : indexStock.keySet()) {
            if (indexStock.get(type) > position && type != productType) {
                indexStock.put(type, indexStock.get(type) + 1);
            }
        }
    }

    // decrements the stock of a certain product type by a certain quantity
    public void decrementProductStock(ProductType productType, int quantity) {
        List<Product> productsToRemove = new ArrayList<>();
        int position = indexStock.getOrDefault(productType, -1);
        if (position == -1) {
            throw new IllegalArgumentException("No stock available for product type: " + productType);
        }

        for (int i = position - 1; i >= 0 && quantity > 0; i--) {
            Product product = products.get(i);
            if (product.getType() == productType) {
                productsToRemove.add(product);
                quantity--;
            }
        }

        if (quantity > 0) {
            throw new IllegalArgumentException("Not enough stock to decrement");
        }

        products.removeAll(productsToRemove);
        indexStock.put(productType, indexStock.get(productType) - productsToRemove.size());

        // Update index for remaining products
        for (ProductType type : indexStock.keySet()) {
            if (indexStock.get(type) > position && type != productType) {
                indexStock.put(type, indexStock.get(type) - productsToRemove.size());
            }
        }
    }

    // returns the ammount of products in stock of a certain product type
    public int getProductCount(ProductType productType) {
        int position = indexStock.getOrDefault(productType, -1);
        if (position == -1) {
            throw new IllegalArgumentException("No stock available for product type: " + productType);
        }

        int count = 0;
        for (int i = position - 1; i >= 0; i--) {
            Product product = products.get(i);
            if (product.getType() == productType) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    public void addSale(Product product) {

        int position = indexSales.getOrDefault(product.getType(), sales.size());
        sales.add(position, product);
        indexSales.put(product.getType(), position + 1);

        // Update index for remaining products
        for (ProductType type : indexSales.keySet()) {
            if (indexSales.get(type) > position && type != product.getType()) {
                indexSales.put(type, indexSales.get(type) + 1);
            }
        }
    }

    // returns the ammount of sales of a certain product type
    public int getSalesCount(ProductType productType) {
        int position = indexSales.getOrDefault(productType, -1);
        if (position == -1) {
            return 0;
        }

        int count = 0;
        for (int i = position - 1; i >= 0; i--) {
            Product product = sales.get(i);
            if (product.getType() == productType) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    @Override
    public String toString() {
        return "Vendor{id=" + id + ", name='" + name + "', address='" + address + "', products=" + products + ", sales=" + sales + "}";
    }
}