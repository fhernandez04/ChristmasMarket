package main.java.de.tum.cit.aet.pse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.de.tum.cit.aet.pse.entity.Vendor;
import main.java.de.tum.cit.aet.pse.service.VendorService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/vendor")
public class VendorController {
    
    @Autowired
    private VendorService vendorService;

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> invoices = (List<Vendor>) vendorService.findAll();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable int id) {
        Vendor vendor = vendorService.findById(id);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }   
    
    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor newVendor) {
        Vendor vendor = vendorService.save(newVendor);
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable int id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        Vendor updatedVendor = vendorService.save(vendor);
        return new ResponseEntity<>(updatedVendor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable int id) {
        vendorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
