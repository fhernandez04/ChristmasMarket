package main.java.de.tum.cit.aet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import jakarta.transaction.Transactional;
import main.java.de.tum.cit.aet.pse.controller.EventController;
import main.java.de.tum.cit.aet.pse.controller.ProductController;
import main.java.de.tum.cit.aet.pse.controller.ShoppingCartController;
import main.java.de.tum.cit.aet.pse.controller.UserController;
import main.java.de.tum.cit.aet.pse.controller.VendorController;
import main.java.de.tum.cit.aet.pse.entity.Event;
import main.java.de.tum.cit.aet.pse.entity.EventType;
import main.java.de.tum.cit.aet.pse.entity.Product;
import main.java.de.tum.cit.aet.pse.entity.ProductFactory;
import main.java.de.tum.cit.aet.pse.entity.ProductType;
import main.java.de.tum.cit.aet.pse.entity.User;
import main.java.de.tum.cit.aet.pse.entity.Vendor;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SolutionMain {

    @Autowired
    private EventController eventController;

    @Autowired
    private ProductController productController;

    @Autowired
    private UserController userController;

    @Autowired
    private VendorController vendorController;

    @Autowired
    private ShoppingCartController shoppingCartController;

    public static void main(String[] args) {
        SpringApplication.run(SolutionMain.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void execCodeAfterStartup() {
        
        // Create test data for Event
        Event event = new Event(1, "Christmas Party", "Annual Christmas party", EventType.SANTAS_GRAND_ARRIVAL);
        eventController.createEvent(event);

        // Create test data for Products
        ProductFactory productFactory = new ProductFactory();
        Product cocoaMug = productFactory.createProduct(ProductType.COCOA);
        Product mulledWine = productFactory.createProduct(ProductType.MULLED_WINE);
        Product sweater = productFactory.createProduct(ProductType.SWEATER);
        Product mulledWine2 = productFactory.createProduct(ProductType.MULLED_WINE);
        Product mulledWine3 = productFactory.createProduct(ProductType.MULLED_WINE);
        Product mulledWine4 = productFactory.createProduct(ProductType.MULLED_WINE);
        Product mulledWine5 = productFactory.createProduct(ProductType.MULLED_WINE);
        Product mulledWine6 = productFactory.createProduct(ProductType.MULLED_WINE);

        productController.createProduct(cocoaMug);
        productController.createProduct(mulledWine);
        productController.createProduct(sweater);
        productController.createProduct(mulledWine2);
        productController.createProduct(mulledWine3);
        productController.createProduct(mulledWine4);
        productController.createProduct(mulledWine5);
        productController.createProduct(mulledWine6);

        // Create test data for User
        User user = new User("John Doe", "johnDoe@gmail.com");
        user.setPocketMoney(100.0);
        userController.createUser(user);

        // Create test data for Vendor
        List<Product> products = new ArrayList<>();
        products.add(cocoaMug);
        products.add(mulledWine);
        products.add(sweater);
        List<Product> sales = new ArrayList<>();
        Vendor vendor1 = new Vendor( "Francisco", "Christmas vendor", products, sales);
        vendorController.createVendor(vendor1);

        List<Product> products2 = new ArrayList<>();
        products2.add(mulledWine2);
        products2.add(mulledWine3);
        products2.add(mulledWine4);
        products2.add(mulledWine5);
        products2.add(mulledWine6);
        List<Product> sales2 = new ArrayList<>();
        Vendor vendor2 = new Vendor( "Javier", "Christmas vendor", products2, sales2);
        vendorController.createVendor(vendor2);
        
        // Simulate adding products to the cart
        shoppingCartController.addToCart(user.getId(), ProductType.COCOA, vendor1.getId(), 1);
        shoppingCartController.addToCart(user.getId(), ProductType.MULLED_WINE, vendor1.getId(), 1);        
        shoppingCartController.addToCart(user.getId(), ProductType.SWEATER, vendor1.getId(), 1);
        shoppingCartController.addToCart(user.getId(), ProductType.MULLED_WINE, vendor2.getId(), 5);
        System.out.println("Shopping Cart :" + user.getShoppingCart());
        System.out.println("Stock of vendor " + vendor1.getName() + " after adding all its products: " + vendor1.getProducts());
        System.out.println("Stock of vendor " + vendor2.getName() + " after adding all its products: " + vendor2.getProducts() + "\n");
        
        // Simulate removing products from the cart
        shoppingCartController.removeFromCart(user.getId(), ProductType.SWEATER, 1);
        System.out.println("Shopping Cart after removing sweater:" + user.getShoppingCart());
        System.out.println("Stock of vendor " + vendor1.getName() + " after removing sweater from the user's cart" + ": " + vendor1.getProducts() + "\n");
        
        // Simulate checkout
        String checkoutResult = shoppingCartController.purchase(user.getId()).getBody();
        System.out.println(checkoutResult);
        System.out.println("Shopping Cart after checkout:" + user.getShoppingCart());

        System.out.println("Vendor " + vendor1.getName() + " has sold: " + "\n" + vendor1.getSalesCount(ProductType.COCOA) + " cocoa mugs");
        System.out.println(vendor1.getSalesCount(ProductType.MULLED_WINE) + " mulled wines");
        System.out.println(vendor1.getSalesCount(ProductType.SWEATER) + " sweaters" + "\n");

        System.out.println("Vendor " + vendor2.getName() + " has sold: " + "\n" + vendor2.getSalesCount(ProductType.COCOA) + " cocoa mugs");
        System.out.println(vendor2.getSalesCount(ProductType.MULLED_WINE) + " mulled wines");
        System.out.println(vendor2.getSalesCount(ProductType.SWEATER) + " sweaters" + "\n");

        //Simulate comsumption of products
        user.consumeProduct(ProductType.MULLED_WINE);
        user.consumeProduct(ProductType.MULLED_WINE);
        user.consumeProduct(ProductType.MULLED_WINE);
        user.consumeProduct(ProductType.MULLED_WINE);
        user.consumeProduct(ProductType.MULLED_WINE);
        user.consumeProduct(ProductType.MULLED_WINE);
        user.consumeProduct(ProductType.COCOA);
        userController.updateUser(user.getId(), user);

        // Test queries
        System.out.println("All Events: " + eventController.getAllEvent().getBody());
        System.out.println("All Products: " + productController.getAllProducts().getBody());
        System.out.println("All Users: " + userController.getAllUser().getBody());
        System.out.println("All Vendors: " + vendorController.getAllVendors().getBody());
        
    }
}