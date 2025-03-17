package main.java.de.tum.cit.aet.pse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.de.tum.cit.aet.pse.entity.Product;
import main.java.de.tum.cit.aet.pse.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    public Product save(Product newProduct) {
        return productRepository.save(newProduct);
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public void deleteById(int productId) {
        try {
            productRepository.deleteById(productId);
        }
        catch (Exception e) {
            System.err.println("Unable to delete Product with ID: " + productId);
        }
    }
}
