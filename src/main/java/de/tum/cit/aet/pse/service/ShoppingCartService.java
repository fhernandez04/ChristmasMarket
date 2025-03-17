package main.java.de.tum.cit.aet.pse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.de.tum.cit.aet.pse.entity.Mediator;
import main.java.de.tum.cit.aet.pse.entity.Product;
import main.java.de.tum.cit.aet.pse.entity.ProductType;
import main.java.de.tum.cit.aet.pse.entity.User;
import main.java.de.tum.cit.aet.pse.entity.Vendor;
import main.java.de.tum.cit.aet.pse.repository.UserRepository;
import main.java.de.tum.cit.aet.pse.repository.VendorRepository;

@Service
public class ShoppingCartService implements Mediator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public String addToCart(int userId, ProductType productType, int vendorId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        List<Product> products = vendor.getProducts();
        List<Product> productsToAdd = new ArrayList<>();
        int productCount = vendor.getProductCount(productType);

        if (productCount < quantity) {
            return "Not enough stock available to add to cart. Available stock: " + productCount;
        }
        
        int stockAvailableIndex = vendor.getIndexStock().get(productType);
        for (int i = stockAvailableIndex - 1; i >= stockAvailableIndex - quantity; i--) {
            Product product = products.get(i);
            if (product.getType() == productType) {
                productsToAdd.add(product);
            }
        }      
        // Adding the product to the user's cart
        for (Product product : productsToAdd) {
            user.addProductToCart(product.getType(), product);
        }

        // Update the stock of the product in the vendor's inventory
        vendor.decrementProductStock(productType, quantity);
        
        userRepository.save(user); // Save the user with the new cart
        vendorRepository.save(vendor); // Save the vendor with the new stock

        return "Product " + productType.toString() + " added to cart";
    }

    @Override
    public String removeFromCart(int userId, ProductType productType, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Product> shoppingCart = user.getShoppingCart();
        List<Product> productsToRemove = new ArrayList<>();

        for (int i = shoppingCart.size() - 1; i >= 0 && productsToRemove.size() < quantity; i--) {
            Product product = shoppingCart.get(i);
            if (product.getType() == productType) {
                productsToRemove.add(product);
            }
        }

        if (productsToRemove.size() < quantity) {
            return "Not enough products in the cart to remove. Available: " + productsToRemove.size();
        }

        for (Product product : productsToRemove) {
            shoppingCart.remove(product);
            Vendor vendor = product.getVendor();
            if (vendor != null) {
                vendor.incrementProductStock(productType, product);
                vendorRepository.save(vendor); // Save the vendor with the updated stock
            }
        }

        userRepository.save(user); // Save the user with the updated cart

        return "Products removed from cart successfully";
    }

    @Override
    public String purchase(int userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        double totalPrice = user.calculateCartPrice();

        if (user.getPocketMoney() < totalPrice) {
            return "Not enough money to complete the purchase";
        }

        // Deduct the money from the user's pocket money
        user.setPocketMoney(user.getPocketMoney() - totalPrice);

        // Register the sale in the vendor's inventory and add products to user's products
        for (Product product : user.getShoppingCart()) {
            Vendor vendor = product.getVendor();
            vendor.addSale(product);
            vendor.increaseMoney(product.getPrice());
            vendorRepository.save(vendor); // Save the vendor with the updated sales
            user.addProduct(product.getType(), product);
        }

        // Clear the user's cart
        user.clearCart();

        userRepository.save(user); // Save the user with the updated pocket money and cleared cart

        return "Checkout completed successfully. Total price: " + totalPrice;
    }
    
}
