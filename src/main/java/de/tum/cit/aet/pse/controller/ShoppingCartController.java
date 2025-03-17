package main.java.de.tum.cit.aet.pse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.java.de.tum.cit.aet.pse.entity.ProductType;
import main.java.de.tum.cit.aet.pse.service.ShoppingCartService;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {
    
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add/{userId}/{productType}")
    public ResponseEntity<String> addToCart(@PathVariable int userId, 
                                            @PathVariable ProductType productType,
                                            @RequestParam int vendorId,  
                                            @RequestParam int quantity) {

        String output = shoppingCartService.addToCart(userId, productType, vendorId, quantity);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{userId}/{productType}")
    public ResponseEntity<String> removeFromCart(@PathVariable int userId, 
                                                 @PathVariable ProductType productType,
                                                 @RequestParam int quantity) {

        String output = shoppingCartService.removeFromCart(userId, productType, quantity);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<String> purchase(@PathVariable int userId) {

        String output = shoppingCartService.purchase(userId);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
