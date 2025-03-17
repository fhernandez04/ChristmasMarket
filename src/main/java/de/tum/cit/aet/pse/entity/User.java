package main.java.de.tum.cit.aet.pse.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Customers")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private int drunkness;
    private double pocketMoney;

    @OneToMany
    private List<Product> shoppingCart = new ArrayList<>();
    // describes the position of the last product of a certain type in the shoppingCart
    // Graphic example: 
    // [COCOA, COCOA, COCOA, COCOA, SWEATER, SWEATER, SWEATER]
    // indexShoppingCart = {COCOA: 4, SWEATER: 7}
    @ElementCollection
    private Map<ProductType, Integer> indexShoppingCart = new HashMap<>();

    @OneToMany
    private List<Product> products = new ArrayList<>();
    // describes the position of the last product of a certain type in the Products
    // Graphic example: 
    // [COCOA, COCOA, COCOA, COCOA, SWEATER, SWEATER, SWEATER]
    // indexProducts = {COCOA: 4, SWEATER: 7}
    @ElementCollection
    private Map<ProductType, Integer> indexProducts = new HashMap<>();

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.drunkness = 0;
        this.shoppingCart = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public User(String name, String email, List<Product> shoppingCart, List<Product> products) {
        this.name = name;
        this.email = email;
        this.drunkness = 0;
        this.indexShoppingCart = new HashMap<>();
        this.products = new ArrayList<>();

        // Sort shoppingCart and configure indexShoppingCart map
        for (Product product : shoppingCart) {
            addProductToCart(product.getType(), product);
        }

        // Sort products and configure indexProducts map
        for (Product product : products) {
            addProduct(product.getType(), product);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDrunkness() {
        return drunkness;
    }

    public void setDrunkness(int drunkness) {
        this.drunkness = drunkness;
    }

    public double getPocketMoney() {
        return pocketMoney;
    }

    public void setPocketMoney(double pocketMoney) {
        this.pocketMoney = pocketMoney;
    }

    public List<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<Product> shoppingCart) {
        this.shoppingCart.clear();
        this.indexShoppingCart.clear();
        for (Product product : products) {
            addProductToCart(product.getType(), product);
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products.clear();
        this.indexProducts.clear();
        for (Product product : products) {
            addProduct(product.getType(), product);
        }
    }

    public Map<ProductType, Integer> getIndexShoppingCart() {
        return indexShoppingCart;
    }

    public Map<ProductType, Integer> getIndexProducts() {
        return indexProducts;
    }

    public void increaseDrunkness() {
        drunkness++;
    }

    public void decreaseDrunkness() {
        drunkness--;
    }

    public double calculateCartPrice() {
        double totalPrice = 0.0;
        for (Product product : shoppingCart) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    public void clearCart() {
        shoppingCart.clear();
        indexShoppingCart.clear();
    }

    // adds a product to the shopping cart
    public void addProductToCart(ProductType productType, Product product) {
        if (product.getType() != productType) {
            throw new IllegalArgumentException("Mismatch between product type and expected type: " + productType);
        }

        int position = indexShoppingCart.getOrDefault(productType, shoppingCart.size());
        shoppingCart.add(position, product);
        indexShoppingCart.put(productType, position + 1);

        // Update index for remaining products
        for (ProductType type : indexShoppingCart.keySet()) {
            if (indexShoppingCart.get(type) > position && type != productType) {
                indexShoppingCart.put(type, indexShoppingCart.get(type) + 1);
            }
        }
    }

    // decrements the ammount of a certain product type by a certain quantity in the shopping cart
    public void removeProductFromCart(ProductType productType, int quantity) {
        List<Product> productsToRemove = new ArrayList<>();
        int position = indexShoppingCart.getOrDefault(productType, -1);
        if (position == -1) {
            throw new IllegalArgumentException("No products available in the cart for product type: " + productType);
        }

        for (int i = position - 1; i >= 0 && quantity > 0; i--) {
            Product product = shoppingCart.get(i);
            if (product.getType() == productType) {
                productsToRemove.add(product);
                quantity--;
            }
        }

        if (quantity > 0) {
            throw new IllegalArgumentException("Not enough products in the cart to remove");
        }

        shoppingCart.removeAll(productsToRemove);
        indexShoppingCart.put(productType, indexShoppingCart.get(productType) - productsToRemove.size());
    
        // Update index for remaining products
        for (ProductType type : indexShoppingCart.keySet()) {
            if (indexShoppingCart.get(type) > position && type != productType) {
                indexShoppingCart.put(type, indexShoppingCart.get(type) - productsToRemove.size());
            }
        }
    }

    // adds a product to the products list
    public void addProduct(ProductType productType, Product product) {
        if (product.getType() != productType) {
            throw new IllegalArgumentException("Mismatch between product type and expected type: " + productType);
        }

        int position = indexProducts.getOrDefault(productType, products.size());
        products.add(position, product);
        indexProducts.put(productType, position + 1);

        // Update index for remaining products
        for (ProductType type : indexProducts.keySet()) {
            if (indexProducts.get(type) > position && type != productType) {
                indexProducts.put(type, indexProducts.get(type) + 1);
            }
        }
    }

    // decrements the ammount of a certain product type by a certain quantity
    public void removeProduct(ProductType productType, int quantity) {
        List<Product> productsToRemove = new ArrayList<>();
        int position = indexProducts.getOrDefault(productType, -1);
        if (position == -1) {
            throw new IllegalArgumentException("No products available for product type: " + productType);
        }

        for (int i = position - 1; i >= 0 && quantity > 0; i--) {
            Product product = products.get(i);
            if (product.getType() == productType) {
                productsToRemove.add(product);
                quantity--;
            }
        }

        if (quantity > 0) {
            throw new IllegalArgumentException("Not enough products to remove");
        }

        products.removeAll(productsToRemove);
        indexProducts.put(productType, indexProducts.get(productType) - productsToRemove.size());
    
        // Update index for remaining products
        for (ProductType type : indexProducts.keySet()) {
            if (indexProducts.get(type) > position && type != productType) {
                indexProducts.put(type, indexProducts.get(type) - productsToRemove.size());
            }
        }
    }

    // returns the ammount of products of a certain product type in the shopping cart
    public int getProductCountShoppingCart(ProductType productType) {
        int position = indexShoppingCart.getOrDefault(productType, -1);
        if (position == -1) {
            throw new IllegalArgumentException("No products available in the cart for product type: " + productType);
        }

        int count = 0;
        for (int i = position - 1; i >= 0; i--) {
            Product product = shoppingCart.get(i);
            if (product.getType() == productType) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    // returns the ammount of products of a certain product type
    public int getProductCountProducts(ProductType productType) {
        int position = indexProducts.getOrDefault(productType, -1);
        if (position == -1) {
            throw new IllegalArgumentException("No products available for product type: " + productType);
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

    public void consumeProduct(ProductType productType) {
        int position = indexProducts.getOrDefault(productType, -1);
        if (position == -1) {
            throw new IllegalArgumentException("You do not have any product of " + productType);
        }

        for (int i = position - 1; i >= 0; i--) {
            Product product = products.get(i);
            if (product.getType() == productType) {
                product.consume(this);
                products.remove(i); // delete the consumed product

                // reduce the index of the products of the same type
                indexProducts.put(productType, indexProducts.get(productType) - 1);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "', shoppingCart=" + shoppingCart + ", products=" + products + "}";
    }
}